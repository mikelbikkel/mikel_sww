
-- Level 5
DROP TABLE IF EXISTS ti_traject_dir;
DROP TABLE IF EXISTS sp_seg_direction;
DROP TABLE IF EXISTS transform_point;
DROP TABLE IF EXISTS direction_override;

-- Level 4
DROP TABLE IF EXISTS landmark_eel;
DROP TABLE IF EXISTS landmark_station;
DROP TABLE IF EXISTS station_eel;
DROP TABLE IF EXISTS sp_seg_stop;
DROP TABLE IF EXISTS ti_traject_stop;
DROP TABLE IF EXISTS direction;
DROP TABLE IF EXISTS location;

-- Level 3
DROP TABLE IF EXISTS landmark;
DROP TABLE IF EXISTS station;
DROP TABLE IF EXISTS ti_traject;
DROP TABLE IF EXISTS sp_segment;
DROP TABLE IF EXISTS eel_noise_words;
DROP TABLE IF EXISTS swwline;
DROP TABLE IF EXISTS city_status;
DROP TABLE IF EXISTS city_info;

-- Level 2
DROP TABLE IF EXISTS city;

-- Level 1
DROP TABLE IF EXISTS sv_type;
DROP TABLE IF EXISTS transport_type;
DROP TABLE IF EXISTS direction_type;
DROP TABLE IF EXISTS station_type;
DROP TABLE IF EXISTS country;
DROP TABLE IF EXISTS continent;

-- Level 1
CREATE TABLE sv_type
( service_type  VARCHAR(25)  NOT NULL
, description   VARCHAR(100)
, PRIMARY KEY (service_type)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

INSERT INTO sv_type(service_type) VALUES('EXTENDED');
INSERT INTO sv_type(service_type) VALUES('PARTIAL');
INSERT INTO sv_type(service_type) VALUES('REGULAR');

CREATE TABLE transport_type
( transport_id  VARCHAR(25)  NOT NULL
, description   VARCHAR(100)
, PRIMARY KEY (transport_id)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

INSERT INTO transport_type(transport_id) VALUES('METRO');
INSERT INTO transport_type(transport_id) VALUES('TRAM');

CREATE TABLE direction_type
( direction_type  VARCHAR(25)  NOT NULL
, description     VARCHAR(100)
, PRIMARY KEY (direction_type)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

INSERT INTO direction_type(direction_type) VALUES('BCK');
INSERT INTO direction_type(direction_type) VALUES('FWD');

CREATE TABLE station_type
( station_type  VARCHAR(25)  NOT NULL
, description     VARCHAR(100)
, PRIMARY KEY (station_type)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
INSERT INTO station_type(station_type) VALUES('REGULAR');
INSERT INTO station_type(station_type) VALUES('COMPLEX');
INSERT INTO station_type(station_type) VALUES('NETWORK');

CREATE TABLE continent
( continent_id  VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, PRIMARY KEY (continent_id)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE country
( country_id    VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, PRIMARY KEY (country_id)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- Level 2
CREATE TABLE city
( city_id       VARCHAR(25)  NOT NULL
, city_code     VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, continent_id  VARCHAR(25)  NOT NULL
, country_id    VARCHAR(25)  NOT NULL
, has_line_info VARCHAR(1)   NOT NULL DEFAULT 'N'
, PRIMARY KEY (city_id)
, CONSTRAINT FK_CITY_CONTINENT FOREIGN KEY (continent_id) 
    REFERENCES continent(continent_id)
, CONSTRAINT FK_CITY_COUNTRY FOREIGN KEY (country_id)
    REFERENCES country(country_id)
, CONSTRAINT UNIQ_CITY_CODE UNIQUE (city_code)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- Level 3
CREATE TABLE  city_info
( id          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT -- mysql variant of auto_increment.
, city_id     VARCHAR(25)  NOT NULL
, url         VARCHAR(255) DEFAULT NULL
, category    VARCHAR(100) DEFAULT NULL
, description VARCHAR(1024) -- longtext is MySQL specific
, indexno     INTEGER NOT NULL DEFAULT 0
, PRIMARY KEY (id)
, CONSTRAINT KEY_CITY_INFO_CITY UNIQUE (city_id)
, CONSTRAINT FK_CITY_INFO_CITY FOREIGN KEY (city_id) 
    REFERENCES city (city_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE city_status
( id             INT(10) UNSIGNED NOT NULL AUTO_INCREMENT -- mysql variant of auto_increment.
, message        VARCHAR(1024) -- mediumtext is MySQL specific
, status         VARCHAR(45) NOT NULL
, ts_status      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
, city_id        VARCHAR(25) NOT NULL
, revisionNeeded DATE
, PRIMARY KEY (id)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE swwline
( line_id      VARCHAR(25)  NOT NULL
, description  VARCHAR(100) NOT NULL
, city_id      VARCHAR(25)  NOT NULL
, service_type VARCHAR(25)  NOT NULL
, remark       VARCHAR(255)
, editinfo     VARCHAR(255)
, PRIMARY KEY (line_id)
, CONSTRAINT FK_LINE_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_LINE_SVTYPE FOREIGN KEY (service_type)
    REFERENCES sv_type(service_type) ON DELETE CASCADE
, CONSTRAINT UNIQ_LINE_DESC UNIQUE (city_id, description)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE eel_noise_words
( id          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT
, noise_word  VARCHAR(75)  NOT NULL
, city_code   VARCHAR(3)
, PRIMARY KEY (id)
, CONSTRAINT FK_EELNW_CITY FOREIGN KEY (city_code)
    REFERENCES city(city_code) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE sp_segment
( segment_id   VARCHAR(25) NOT NULL
, city_id      VARCHAR(25) NOT NULL
, transport_id VARCHAR(25) NOT NULL
, PRIMARY KEY (segment_id)
, CONSTRAINT FK_SEG_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_SEG_TRANSTYPE FOREIGN KEY (transport_id)
    REFERENCES transport_type(transport_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE ti_traject
( traject_id   varchar(25) NOT NULL
, city_id      varchar(25) NOT NULL
, transport_id varchar(25) NOT NULL
, PRIMARY KEY (traject_id)
, CONSTRAINT FK_TRAJECT_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_TRAJECT_TRANSPORT FOREIGN KEY (transport_id)
    REFERENCES transport_type (transport_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE station
( station_id    VARCHAR(75)  NOT NULL
, description   VARCHAR(100) NOT NULL
, city_id       VARCHAR(25)  NOT NULL
, node_id       VARCHAR(75)
, PRIMARY KEY (station_id)
, CONSTRAINT FK_STATION_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark
( landmark_id   VARCHAR(75)  NOT NULL
, description   VARCHAR(100) NOT NULL
, city_id       VARCHAR(25)  NOT NULL
, PRIMARY KEY (landmark_id)
, CONSTRAINT FK_LANDMARK_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- Level 4
CREATE TABLE location
( location_id VARCHAR(75) NOT NULL
, latitude DOUBLE PRECISION -- "double" is mysql-specific. "double precision" is ansi sql. DECIMAL(p,s) is also ansi.
, longitude DOUBLE PRECISION
, location_type VARCHAR(10) NOT NULL
, localname VARCHAR(255)
, PRIMARY KEY (location_id)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE direction
( dir_id        VARCHAR(25)  NOT NULL
, city_id       VARCHAR(25)  NOT NULL
, description   VARCHAR(100) NOT NULL
, line_id       VARCHAR(25)  NOT NULL
, endpoint      VARCHAR(100) NOT NULL
, PRIMARY KEY (dir_id)
, CONSTRAINT FK_DIR_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_DIR_LINE FOREIGN KEY (line_id)
    REFERENCES swwline(line_id) ON DELETE CASCADE
, CONSTRAINT UNIQ_DIR_DESC UNIQUE (city_id, description)
, CONSTRAINT UNIQ_LINE_END UNIQUE (line_id, endpoint)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE ti_traject_stop
( traject_id    varchar(25) NOT NULL
, station_id    varchar(75) NOT NULL
, stop_sequence INTEGER     NOT NULL
, PRIMARY KEY (traject_id,stop_sequence)
, CONSTRAINT FK_STOP_STATION FOREIGN KEY (station_id)
    REFERENCES station (station_id) ON DELETE CASCADE
, CONSTRAINT FK_STOP_TRAJECT FOREIGN KEY (traject_id)
    REFERENCES ti_traject (traject_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE sp_seg_stop
( segment_id  VARCHAR(25)  NOT NULL
, seqno       INTEGER      NOT NULL
, station_id  VARCHAR(75)  NOT NULL
, PRIMARY KEY (segment_id, seqno)
, CONSTRAINT FK_SEGSTOP_STATION FOREIGN KEY (station_id)
    REFERENCES station(station_id) ON DELETE CASCADE
, CONSTRAINT FK_SEGSTOP_SEGMENT FOREIGN KEY (segment_id)
    REFERENCES sp_segment(segment_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE station_eel
( station_id   VARCHAR(75) NOT NULL
, eel           VARCHAR(1) NOT NULL
, PRIMARY KEY (station_id, eel)
, CONSTRAINT FK_STATEEL_STATION FOREIGN KEY (station_id)
    REFERENCES station(station_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark_station
( landmark_id   VARCHAR(75)  NOT NULL
, station_id    VARCHAR(75)  NOT NULL
, PRIMARY KEY (landmark_id, station_id)
, CONSTRAINT FK_LMSTAT_LANDMARK FOREIGN KEY (landmark_id)
    REFERENCES landmark(landmark_id)  ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE landmark_eel
( landmark_id   VARCHAR(75)  NOT NULL
, eel           CHAR(1)      NOT NULL
, PRIMARY KEY (landmark_id, eel)
, CONSTRAINT FK_LMEEL_LANDMARK FOREIGN KEY (landmark_id)
    REFERENCES landmark(landmark_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- Level 5
CREATE TABLE direction_override
( station_id      varchar(75) NOT NULL
, dir_id          varchar(25) NOT NULL
, override_dir_id varchar(25) NOT NULL
, PRIMARY KEY (station_id,dir_id)
, CONSTRAINT FK_DIR_2_OVER_DIR FOREIGN KEY (override_dir_id)
    REFERENCES direction (dir_id) ON DELETE CASCADE
, CONSTRAINT FK_DIR_OVER_DIR FOREIGN KEY (dir_id)
    REFERENCES direction (dir_id) ON DELETE CASCADE
, CONSTRAINT FK_DIR_OVER_STAT FOREIGN KEY (station_id)
    REFERENCES station (station_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE transform_point
( pre_dir_id  VARCHAR(25)  NOT NULL
, station_id  VARCHAR(75)  NOT NULL
, post_dir_id VARCHAR(25)  NOT NULL
, PRIMARY KEY (pre_dir_id, station_id, post_dir_id)
, CONSTRAINT FK_TRFM_DIR_PRE FOREIGN KEY (pre_dir_id)
    REFERENCES direction(dir_id) ON DELETE CASCADE
, CONSTRAINT FK_TRFM_DIR_POST FOREIGN KEY (post_dir_id)
    REFERENCES direction(dir_id) ON DELETE CASCADE
, CONSTRAINT FK_TRFM_DIR_STATION
    FOREIGN KEY (station_id)  REFERENCES station(station_id)
		ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE sp_seg_direction
( segment_id     VARCHAR(25)  NOT NULL
, dir_id         VARCHAR(25)  NOT NULL
, direction_type VARCHAR(3)   NOT NULL -- Domain: FWD, BCK.
, PRIMARY KEY (segment_id, dir_id, direction_type)
, CONSTRAINT FK_SEGDIR_DIR FOREIGN KEY (dir_id)
    REFERENCES direction(dir_id) ON DELETE CASCADE
, CONSTRAINT FK_SEGDIR_SEGMENT FOREIGN KEY (segment_id)
    REFERENCES sp_segment(segment_id) ON DELETE CASCADE
, CONSTRAINT FK_SEGDIR_DIR_TYPE FOREIGN KEY (direction_type)
    REFERENCES direction_type(direction_type) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE ti_traject_dir
( traject_id varchar(25) NOT NULL
, fwd_dir_id varchar(25) NOT NULL
, PRIMARY KEY (traject_id, fwd_dir_id)
, CONSTRAINT FK_TRJDIR_DIRECTION FOREIGN KEY (fwd_dir_id)
    REFERENCES direction (dir_id) ON DELETE CASCADE
, CONSTRAINT FK_TRJDIR_TRAJECT FOREIGN KEY (traject_id)
    REFERENCES ti_traject (traject_id) ON DELETE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- End of file
