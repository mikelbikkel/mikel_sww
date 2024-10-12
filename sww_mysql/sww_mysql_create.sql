--
-- Created: 12-12-2009.
--
-- Script to create sww (SubWayWorld) database objects.
--
-- Run this script as sww_owner.
-- mysql -u sww_owner -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- ANSI SQL:
-- SMALLINT, INTEGER OR INT
-- FLOAT, REAL and DOUBLE PRECISION => FLOAT(126)
-- NUMERIC(precision, scale) or DECIMAL(precision, scale) (e.g. 1234.56) => NUMBER(p,s)
--
-- Remarks:
-- VARCHAR(10) mysql: unit is characters.
--
-- Version Date     Who  Remark
-- 1.0     12-12-09 MVD  Initial version.
-- 1.1     19-03-10 MVD  station_id, node_id: length from 25 to 75.
-- 1.2     12-10-24 MVD  Replace VARCHAR with CHARACTER VARYING.
--                       Replace DOUBLE  with DECIMAL(9,6). No float for latitude/longitude.
--                       Replace INT(10) UNSIGNED with INTEGER CHECK ( x >= 0)
--                       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT => INTEGER GENERATED ALWAYS AS IDENTITY
--                               LONGTEXT => CHARACTER VARYING(1024)
--
-- SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
-- SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
-- SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

-- For Oracle, make sure to set:
-- alter session set nls_length_semantics = 'CHAR';
-- Optionally: V$NLS_VALID_VALUES
-- alter session set nls_language = 'DUTCH';
-- alter session set nls_territory = 'THE NETHERLANDS';
-- alter session set nls_date_format = 'DD-MM-YYYY';
-- NLS_LANG=language_territory.charset


-- INT(10) UNSIGNED
DROP TABLE IF EXISTS sp_seg_direction;
DROP TABLE IF EXISTS sp_seg_stop;
DROP TABLE IF EXISTS sp_segment;
DROP TABLE IF EXISTS transform_point;
DROP TABLE IF EXISTS direction;
DROP TABLE IF EXISTS swwline;
DROP TABLE IF EXISTS landmark_station;
DROP TABLE IF EXISTS landmark_eel;
DROP TABLE IF EXISTS landmark;
DROP TABLE IF EXISTS station_eel;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS eel_noise_words;
DROP TABLE IF EXISTS city_info;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS sv_type;
DROP TABLE IF EXISTS continent;
DROP TABLE IF EXISTS country;
DROP TABLE IF EXISTS transport_type;

CREATE TABLE sv_type
( service_type  CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100)
, PRIMARY KEY (service_type)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE continent
( continent_id  CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, PRIMARY KEY (continent_id)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE country
( country_id    CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, PRIMARY KEY (country_id)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE transport_type
( transport_id  CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, PRIMARY KEY (transport_id)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE city
( city_id       CHARACTER VARYING(25)  NOT NULL
, city_code     CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, continent_id  CHARACTER VARYING(25)  NOT NULL
, country_id    CHARACTER VARYING(25)  NOT NULL
, has_line_info CHARACTER VARYING(1)   DEFAULT 'N' NOT NULL 
, latitude      DECIMAL(9,6)
, longitude		  DECIMAL(9,6)
, PRIMARY KEY (city_id)
, CONSTRAINT FK_CITY_CONTINENT
    FOREIGN KEY (continent_id) REFERENCES continent(continent_id)
, CONSTRAINT FK_CITY_COUNTRY
    FOREIGN KEY (country_id)   REFERENCES country(country_id)
, CONSTRAINT UNIQ_CITY_CODE UNIQUE (city_code)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE  city_info
( id          INTEGER GENERATED ALWAYS AS IDENTITY
, city_id     CHARACTER VARYING(25)  NOT NULL
, url         CHARACTER VARYING(255)
, category    CHARACTER VARYING(100)
, description CHARACTER VARYING(1024)
, indexno     INTEGER DEFAULT 0 NOT NULL 
, PRIMARY KEY (id)
, CONSTRAINT UNIQ_CITY_ID UNIQUE(city_id)
, CONSTRAINT FK_CITY_INFO_CITY FOREIGN KEY (city_id) 
	  REFERENCES city (city_id) ON DELETE CASCADE
,  CONSTRAINT CHK_INDEXNO CHECK(indexno >= 0)
);
-- ENGINE=InnoDB
-- AUTO_INCREMENT=220
-- DEFAULT CHARSET=utf8;

CREATE TABLE eel_noise_words
( id          INTEGER GENERATED ALWAYS AS IDENTITY
, noise_word  CHARACTER VARYING(75)  NOT NULL
, city_code   CHARACTER VARYING(3)
, PRIMARY KEY (id)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE station
( station_id    CHARACTER VARYING(75)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, city_id       CHARACTER VARYING(25)  NOT NULL
, node_id       CHARACTER VARYING(75)
, local_name    CHARACTER VARYING(100)
, latitude      DECIMAL(9,6)
, longitude     DECIMAL(9,6)
, PRIMARY KEY (station_id)
, CONSTRAINT FK_STATION_CITY
    FOREIGN KEY (city_id) REFERENCES city(city_id)
		ON DELETE CASCADE
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE station_eel
( station_id   CHARACTER VARYING(75)  NOT NULL
, eel          CHARACTER VARYING(1)   NOT NULL
, PRIMARY KEY (station_id, eel)
, CONSTRAINT FK_STATEEL_STATION
    FOREIGN KEY (station_id) REFERENCES station(station_id)
		ON DELETE CASCADE
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark
( landmark_id   CHARACTER VARYING(75)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, city_id       CHARACTER VARYING(25)  NOT NULL
, latitude      DECIMAL(9,6)
, longitude     DECIMAL(9,6)
, PRIMARY KEY (landmark_id)
, CONSTRAINT FK_LANDMARK_CITY
    FOREIGN KEY (city_id) REFERENCES city(city_id)
		ON DELETE CASCADE
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark_station
( landmark_id   CHARACTER VARYING(75)  NOT NULL
, station_id    CHARACTER VARYING(75)  NOT NULL
, PRIMARY KEY (landmark_id, station_id)
, CONSTRAINT FK_LMSTAT_LANDMARK
    FOREIGN KEY (landmark_id) REFERENCES landmark(landmark_id)
		ON DELETE CASCADE
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark_eel
( landmark_id   CHARACTER VARYING(75)  NOT NULL
, eel           CHARACTER VARYING(1)   NOT NULL
, PRIMARY KEY (landmark_id, eel)
, CONSTRAINT FK_LMEEL_LANDMARK
    FOREIGN KEY (landmark_id) REFERENCES landmark(landmark_id)
		ON DELETE CASCADE
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE swwline
( line_id      CHARACTER VARYING(25)  NOT NULL
, description  CHARACTER VARYING(100) NOT NULL
, city_id      CHARACTER VARYING(25)  NOT NULL
, service_type CHARACTER VARYING(25)  NOT NULL
, remark       CHARACTER VARYING(255)
, editinfo     CHARACTER VARYING(255)
, PRIMARY KEY (line_id)
, CONSTRAINT FK_LINE_CITY
    FOREIGN KEY (city_id)      REFERENCES city(city_id)
		ON DELETE CASCADE
, CONSTRAINT FK_LINE_SVTYPE
    FOREIGN KEY (service_type) REFERENCES sv_type(service_type)
		ON DELETE CASCADE
, CONSTRAINT UNIQ_LINE_DESC UNIQUE (city_id, description)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;
 
-- TODO:
-- remove city_id + FK
CREATE TABLE direction
( dir_id        CHARACTER VARYING(25)  NOT NULL
, city_id       CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, line_id       CHARACTER VARYING(25)  NOT NULL
, endpoint      CHARACTER VARYING(100) NOT NULL
, PRIMARY KEY (dir_id)
, CONSTRAINT FK_DIR_CITY
    FOREIGN KEY (city_id)      REFERENCES city(city_id)
		ON DELETE CASCADE
, CONSTRAINT FK_DIR_LINE
    FOREIGN KEY (line_id) REFERENCES swwline(line_id)
		ON DELETE CASCADE
, CONSTRAINT UNIQ_DIR_DESC UNIQUE (city_id, description)
, CONSTRAINT UNIQ_LINE_END UNIQUE (line_id, endpoint)
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE transform_point
( pre_dir_id  CHARACTER VARYING(25)  NOT NULL
, station_id  CHARACTER VARYING(75)  NOT NULL
, post_dir_id CHARACTER VARYING(25)  NOT NULL
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
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

--
-- Tables related to shortest path.
-- Have prefix sp_
--
CREATE TABLE sp_segment
( segment_id   CHARACTER VARYING(25) NOT NULL
, city_id      CHARACTER VARYING(25) NOT NULL
, transport_id CHARACTER VARYING(25) NOT NULL
, PRIMARY KEY (segment_id)
, CONSTRAINT FK_SEG_CITY
    FOREIGN KEY (city_id)      REFERENCES city(city_id)
		ON DELETE CASCADE
, CONSTRAINT FK_SEG_TRANSTYPE
    FOREIGN KEY (transport_id) REFERENCES transport_type(transport_id)
		ON DELETE CASCADE
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE sp_seg_stop
( segment_id  CHARACTER VARYING(25)  NOT NULL
, seqno       INTEGER      NOT NULL
, station_id  CHARACTER VARYING(75)  NOT NULL
, PRIMARY KEY (segment_id, seqno)
, CONSTRAINT FK_SEGSTOP_STATION
    FOREIGN KEY (station_id) REFERENCES station(station_id)
		ON DELETE CASCADE
, CONSTRAINT FK_SEGSTOP_SEGMENT
    FOREIGN KEY (segment_id) REFERENCES sp_segment(segment_id)
		ON DELETE CASCADE
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

CREATE TABLE sp_seg_direction
( segment_id     CHARACTER VARYING(25) NOT NULL
, dir_id         CHARACTER VARYING(25) NOT NULL
, direction_type CHARACTER VARYING(3)  NOT NULL
, PRIMARY KEY (segment_id, dir_id, direction_type)
, CONSTRAINT FK_SEGDIR_DIR
    FOREIGN KEY (dir_id)     REFERENCES direction(dir_id)
		ON DELETE CASCADE
, CONSTRAINT FK_SEGDIR_SEGMENT
    FOREIGN KEY (segment_id) REFERENCES sp_segment(segment_id)
		ON DELETE CASCADE
, CONSTRAINT ENUM_DIR_TYPE CHECK (direction_type in ('FWD', 'BCK')) 
);
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8;

-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- End of file

