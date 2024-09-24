--
-- Created: 12-12-2009.
--
-- Script to create sww (SubWayWorld) database objects.
--
-- Run this script as sww_owner.
-- mysql -u sww_owner -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- Version Date     Who  Remark
-- 1.0     12-12-09 MVD  Initial version.
-- 1.1     19-03-10 MVD  station_id, node_id: length from 25 to 75.
--
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP TABLE IF EXISTS sv_type;
CREATE TABLE sv_type
(
  service_type  VARCHAR(25)  NOT NULL
, description   VARCHAR(100)
, PRIMARY KEY (service_type)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS continent;
CREATE TABLE continent
(
  continent_id  VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, PRIMARY KEY (continent_id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS country;
CREATE TABLE country
(
  country_id    VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, PRIMARY KEY (country_id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS transport_type;
CREATE TABLE transport_type
(
  transport_id  VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, PRIMARY KEY (transport_id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS city;
CREATE TABLE city
(
  city_id       VARCHAR(25)  NOT NULL
, city_code     VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, continent_id  VARCHAR(25)  NOT NULL
, country_id    VARCHAR(25)  NOT NULL
, has_line_info VARCHAR(1)   NOT NULL DEFAULT 'N'
, latitude		DOUBLE
, longitude		DOUBLE
, PRIMARY KEY (city_id)
, CONSTRAINT FK_CITY_CONTINENT
    FOREIGN KEY (continent_id) REFERENCES continent(continent_id)
, CONSTRAINT FK_CITY_COUNTRY
    FOREIGN KEY (country_id)   REFERENCES country(country_id)
, CONSTRAINT UNIQ_CITY_CODE UNIQUE (city_code)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS city_info;
CREATE TABLE  city_info (
  id          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT
, city_id     VARCHAR(25)  NOT NULL
, url         VARCHAR(255) DEFAULT NULL
, category    VARCHAR(100) DEFAULT NULL
, description LONGTEXT
, indexno     INT(10) UNSIGNED NOT NULL DEFAULT '0'
, PRIMARY KEY (id)
, KEY KEY_CITY_INFO_CITY (city_id)
, CONSTRAINT FK_CITY_INFO_CITY FOREIGN KEY (city_id) 
	  REFERENCES city (city_id) ON DELETE CASCADE
)
ENGINE=InnoDB
AUTO_INCREMENT=220
DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS eel_noise_words;
CREATE TABLE eel_noise_words
(
  id          INT UNSIGNED NOT NULL AUTO_INCREMENT
, noise_word  VARCHAR(75)  NOT NULL
, city_code   VARCHAR(3)
, PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS station_eel;
DROP TABLE IF EXISTS station;
CREATE TABLE station
(
  station_id    VARCHAR(75)  NOT NULL
, description   VARCHAR(100) NOT NULL
, city_id       VARCHAR(25)  NOT NULL
, node_id       VARCHAR(75)
, local_name    VARCHAR(100)
, latitude      DOUBLE
, longitude     DOUBLE
, PRIMARY KEY (station_id)
, CONSTRAINT FK_STATION_CITY
    FOREIGN KEY (city_id) REFERENCES city(city_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE station_eel
(
  station_id   VARCHAR(75)  NOT NULL
, eel           VARCHAR(1)   NOT NULL
, PRIMARY KEY (station_id, eel)
, CONSTRAINT FK_STATEEL_STATION
    FOREIGN KEY (station_id) REFERENCES station(station_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS landmark_station;
DROP TABLE IF EXISTS landmark_eel;
DROP TABLE IF EXISTS landmark;
CREATE TABLE landmark
(
  landmark_id   VARCHAR(75)  NOT NULL
, description   VARCHAR(100) NOT NULL
, city_id       VARCHAR(25)  NOT NULL
, latitude      DOUBLE
, longitude     DOUBLE
, PRIMARY KEY (landmark_id)
, CONSTRAINT FK_LANDMARK_CITY
    FOREIGN KEY (city_id) REFERENCES city(city_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark_station
(
  landmark_id   VARCHAR(75)  NOT NULL
, station_id    VARCHAR(75)  NOT NULL
, PRIMARY KEY (landmark_id, station_id)
, CONSTRAINT FK_LMSTAT_LANDMARK
    FOREIGN KEY (landmark_id) REFERENCES landmark(landmark_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark_eel
(
  landmark_id   VARCHAR(75)  NOT NULL
, eel           CHAR(1)      NOT NULL
, PRIMARY KEY (landmark_id, eel)
, CONSTRAINT FK_LMEEL_LANDMARK
    FOREIGN KEY (landmark_id) REFERENCES landmark(landmark_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS alias_station;

DROP TABLE IF EXISTS swwline;
CREATE TABLE swwline
(
  line_id      VARCHAR(25)  NOT NULL
, description  VARCHAR(100) NOT NULL
, city_id      VARCHAR(25)  NOT NULL
, service_type VARCHAR(25)  NOT NULL
, remark       VARCHAR(255)
, editinfo     VARCHAR(255)
, PRIMARY KEY (line_id)
, CONSTRAINT FK_LINE_CITY
    FOREIGN KEY (city_id)      REFERENCES city(city_id)
		ON DELETE CASCADE
, CONSTRAINT FK_LINE_SVTYPE
    FOREIGN KEY (service_type) REFERENCES sv_type(service_type)
		ON DELETE CASCADE
, CONSTRAINT UNIQ_LINE_DESC UNIQUE (city_id, description)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
 
-- TODO:
-- remove city_id + FK
DROP TABLE IF EXISTS direction;
CREATE TABLE direction
(
  dir_id        VARCHAR(25)  NOT NULL
, city_id       VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, line_id       VARCHAR(25)  NOT NULL
, endpoint      VARCHAR(100) NOT NULL
, PRIMARY KEY (dir_id)
, CONSTRAINT FK_DIR_CITY
    FOREIGN KEY (city_id)      REFERENCES city(city_id)
		ON DELETE CASCADE
, CONSTRAINT FK_DIR_LINE
    FOREIGN KEY (line_id) REFERENCES swwline(line_id)
		ON DELETE CASCADE
, CONSTRAINT UNIQ_DIR_DESC UNIQUE (city_id, description)
, CONSTRAINT UNIQ_LINE_END UNIQUE (line_id, endpoint)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS transform_point;
CREATE TABLE transform_point
(
  pre_dir_id  VARCHAR(25)  NOT NULL
, station_id  VARCHAR(75)  NOT NULL
, post_dir_id VARCHAR(25)  NOT NULL
, PRIMARY KEY (pre_dir_id, station_id, post_dir_id)
, CONSTRAINT FK_TRFM_DIR_PRE
    FOREIGN KEY (pre_dir_id)  REFERENCES direction(dir_id)
		ON DELETE CASCADE
, CONSTRAINT FK_TRFM_DIR_POST
    FOREIGN KEY (post_dir_id) REFERENCES direction(dir_id)
		ON DELETE CASCADE
, CONSTRAINT FK_TRFM_DIR_STATION
    FOREIGN KEY (station_id)  REFERENCES station(station_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


--
-- Tables related to shortest path.
-- Have prefix sp_
--
DROP TABLE IF EXISTS sp_segment;
CREATE TABLE sp_segment
(
  segment_id   VARCHAR(25) NOT NULL
, city_id      VARCHAR(25) NOT NULL
, transport_id VARCHAR(25) NOT NULL
, PRIMARY KEY (segment_id)
, CONSTRAINT FK_SEG_CITY
    FOREIGN KEY (city_id)      REFERENCES city(city_id)
		ON DELETE CASCADE
, CONSTRAINT FK_SEG_TRANSTYPE
    FOREIGN KEY (transport_id) REFERENCES transport_type(transport_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS sp_seg_stop;
CREATE TABLE sp_seg_stop
(
  segment_id  VARCHAR(25)  NOT NULL
, seqno       INTEGER      NOT NULL
, station_id  VARCHAR(75)  NOT NULL
, PRIMARY KEY (segment_id, seqno)
, CONSTRAINT FK_SEGSTOP_STATION
    FOREIGN KEY (station_id) REFERENCES station(station_id)
		ON DELETE CASCADE
, CONSTRAINT FK_SEGSTOP_SEGMENT
    FOREIGN KEY (segment_id) REFERENCES sp_segment(segment_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS sp_seg_direction;
CREATE TABLE sp_seg_direction
(
  segment_id     VARCHAR(25)        NOT NULL
, dir_id         VARCHAR(25)        NOT NULL
, direction_type ENUM('FWD', 'BCK') NOT NULL
, PRIMARY KEY (segment_id, dir_id, direction_type)
, CONSTRAINT FK_SEGDIR_DIR
    FOREIGN KEY (dir_id)     REFERENCES direction(dir_id)
		ON DELETE CASCADE
, CONSTRAINT FK_SEGDIR_SEGMENT
    FOREIGN KEY (segment_id) REFERENCES sp_segment(segment_id)
		ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- End of file

