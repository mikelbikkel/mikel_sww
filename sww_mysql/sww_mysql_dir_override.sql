--
-- Script to create directin override table.
--
-- Run this script:
-- mysql -u UNAME -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- Version Date     Who  Remark
-- 1.0     27-12-12 MVD  Initial version.
--
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

-- Station overrides for a specific direction.
-- direction.endpoint and swwline.description
DROP TABLE IF EXISTS direction_override;
CREATE TABLE direction_override
(
  station_id      VARCHAR(75)  NOT NULL
, dir_id          VARCHAR(25)  NOT NULL
, override_dir_id VARCHAR(25)  NOT NULL
, PRIMARY KEY (station_id, dir_id)
, CONSTRAINT FK_DIR_OVER_STAT
    FOREIGN KEY (station_id)      REFERENCES station(station_id)
    ON DELETE CASCADE
, CONSTRAINT FK_DIR_OVER_DIR
    FOREIGN KEY (dir_id)          REFERENCES direction(dir_id)
    ON DELETE CASCADE
, CONSTRAINT FK_DIR_2_OVER_DIR
    FOREIGN KEY (override_dir_id) REFERENCES direction(dir_id)
    ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS ti_traject_stop;
DROP TABLE IF EXISTS ti_traject_dir;
DROP TABLE IF EXISTS ti_traject;

DROP TABLE IF EXISTS ti_traject;
CREATE TABLE ti_traject
(
  traject_id   VARCHAR(25)  NOT NULL
, city_id      VARCHAR(25)  NOT NULL
, transport_id VARCHAR(25)  NOT NULL
, PRIMARY KEY (traject_id)
, CONSTRAINT FK_TRAJECT_CITY
    FOREIGN KEY (city_id)      REFERENCES city(city_id)
    ON DELETE CASCADE
, CONSTRAINT FK_TRAJECT_TRANSPORT
    FOREIGN KEY (transport_id) REFERENCES transport_type(transport_id)
    ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
 
DROP TABLE IF EXISTS ti_traject_stop;
CREATE TABLE ti_traject_stop
(
  traject_id    VARCHAR(25)      NOT NULL
, station_id    VARCHAR(75)      NOT NULL
, stop_sequence INT(10) UNSIGNED NOT NULL
, PRIMARY KEY (traject_id, stop_sequence)
, CONSTRAINT FK_STOP_TRAJECT
    FOREIGN KEY (traject_id) REFERENCES ti_traject(traject_id)
    ON DELETE CASCADE
, CONSTRAINT FK_STOP_STATION
    FOREIGN KEY (station_id) REFERENCES station(station_id)
    ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS ti_traject_dir;
CREATE TABLE ti_traject_dir
(
  traject_id   VARCHAR(25) NOT NULL
, fwd_dir_id   VARCHAR(25) NOT NULL
, PRIMARY KEY (traject_id, fwd_dir_id)
, CONSTRAINT FK_TRJDIR_TRAJECT
    FOREIGN KEY (traject_id) REFERENCES ti_traject(traject_id)
    ON DELETE CASCADE
, CONSTRAINT FK_TRJDIR_DIRECTION
    FOREIGN KEY (fwd_dir_id) REFERENCES direction(dir_id)
    ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP VIEW IF EXISTS rs_seg_directions;
CREATE VIEW rs_seg_directions
AS
SELECT sd.segment_id
     , sd.dir_id
     , d.description
		 , sd.direction_type
		 , d.city_id
FROM   sp_seg_direction sd join direction d on sd.dir_id = d.dir_id
;

DROP VIEW IF EXISTS rs_dir_override;
CREATE VIEW rs_dir_override
AS
SELECT dir.city_id
     , tstop.traject_id
		 , tstop.stop_sequence
     , dirover.station_id
		 , dirover.dir_id
		 , dirover.override_dir_id
		 , ln.description line
		 , dir.endpoint
		 , ln.service_type
		 , ln.remark
FROM direction_override dirover
     JOIN station s ON dirover.station_id = s.station_id
		 JOIN direction dir ON dirover.override_dir_id = dir.dir_id
		 JOIN swwline ln ON dir.line_id = ln.line_id
		 JOIN ti_traject_dir tdir on tdir.fwd_dir_id = dirover.dir_id
		 JOIN ti_traject_stop tstop on tstop.traject_id = tdir.traject_id
		      and tstop.station_id = dirover.station_id
;

DROP VIEW IF EXISTS rs_dir_no_override;
CREATE VIEW rs_dir_no_override
AS
SELECT dir.city_id
     , tstop.traject_id
		 , tstop.stop_sequence
     , tstop.station_id
		 , traj.fwd_dir_id dir_id
		 , NULL override_dir_id
		 , ln.description line
		 , dir.endpoint
		 , ln.service_type
		 , ln.remark
FROM ti_traject_stop tstop
     JOIN ti_traject_dir traj ON tstop.traject_id = traj.traject_id
		 JOIN direction dir ON traj.fwd_dir_id = dir.dir_id
		 JOIN swwline ln ON dir.line_id = ln.line_id
WHERE NOT EXISTS (select 'x'
                  FROM   direction_override dir2
									WHERE  dir2.station_id = tstop.station_id
									AND    dir2.dir_id = traj.fwd_dir_id)
;

DROP VIEW IF EXISTS rs_network_station;
CREATE VIEW rs_network_station
AS
SELECT s.station_id
     , s.description
     , city_id
     , loc.localname as local_name
     , loc.latitude
     , loc.longitude
		 , s.station_type
		 , s.node_id
FROM   station s 
         LEFT OUTER JOIN location loc ON s.station_id = loc.location_id
;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- End of file
