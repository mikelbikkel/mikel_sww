-- Create tables for the SWW database.
-- ANSI SQL [is the intention].
-- 
-- 2025-01-12  Michel       Start with postgresql version and make data types ANSI compliant.
--

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
( service_type  CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100)
, PRIMARY KEY (service_type)
);
INSERT INTO sv_type(service_type) VALUES('EXTENDED');
INSERT INTO sv_type(service_type) VALUES('PARTIAL');
INSERT INTO sv_type(service_type) VALUES('REGULAR');

CREATE TABLE transport_type
( transport_id  CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100)
, PRIMARY KEY (transport_id)
);
INSERT INTO transport_type(transport_id) VALUES('METRO');
INSERT INTO transport_type(transport_id) VALUES('TRAM');

CREATE TABLE direction_type
( direction_type  CHARACTER VARYING(25)  NOT NULL
, description     CHARACTER VARYING(100)
, PRIMARY KEY (direction_type)
);
INSERT INTO direction_type(direction_type) VALUES('BCK');
INSERT INTO direction_type(direction_type) VALUES('FWD');

CREATE TABLE station_type
( station_type  CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100)
, PRIMARY KEY (station_type)
);
INSERT INTO station_type(station_type) VALUES('REGULAR');
INSERT INTO station_type(station_type) VALUES('COMPLEX');
INSERT INTO station_type(station_type) VALUES('NETWORK');

CREATE TABLE continent
( continent_id  CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, PRIMARY KEY (continent_id)
);

CREATE TABLE country
( country_id    CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, PRIMARY KEY (country_id)
);

-- Level 2
CREATE TABLE city
( city_id       CHARACTER VARYING(25)  NOT NULL
, city_code     CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, continent_id  CHARACTER VARYING(25)  NOT NULL
, country_id    CHARACTER VARYING(25)  NOT NULL
, has_line_info CHARACTER VARYING(1)   NOT NULL DEFAULT 'N'
, PRIMARY KEY (city_id)
, CONSTRAINT FK_CITY_CONTINENT FOREIGN KEY (continent_id) 
    REFERENCES continent(continent_id)
, CONSTRAINT FK_CITY_COUNTRY FOREIGN KEY (country_id)
    REFERENCES country(country_id)
, CONSTRAINT UNIQ_CITY_CODE UNIQUE (city_code)
);

-- Level 3
drop table city_info;
CREATE TABLE  city_info
( id          INTEGER GENERATED ALWAYS AS IDENTITY
, city_id     CHARACTER VARYING(25)  NOT NULL
, category    CHARACTER VARYING(100) NOT NULL
, url         CHARACTER VARYING(255)
, description CHARACTER VARYING(1024) -- longtext is MySQL specific
, indexno     INTEGER NOT NULL DEFAULT 0
, valid_url   INTEGER NOT NULL DEFAULT 1
, PRIMARY KEY (id)
, CONSTRAINT UK_CITY_INFO UNIQUE (city_id, category, indexno)
, CONSTRAINT FK_CITY_INFO_CITY FOREIGN KEY (city_id) 
    REFERENCES city (city_id) ON DELETE CASCADE
);

CREATE TABLE city_status
( id             INTEGER GENERATED ALWAYS AS IDENTITY
, message        CHARACTER VARYING(1024) -- mediumtext is MySQL specific
, status         CHARACTER VARYING(45) NOT NULL
, ts_status      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
, city_id        CHARACTER VARYING(25) NOT NULL
, revisionNeeded DATE
, PRIMARY KEY (id)
);

CREATE TABLE swwline
( line_id      CHARACTER VARYING(25)  NOT NULL
, description  CHARACTER VARYING(100) NOT NULL
, city_id      CHARACTER VARYING(25)  NOT NULL
, service_type CHARACTER VARYING(25)  NOT NULL
, remark       CHARACTER VARYING(255)
, editinfo     CHARACTER VARYING(255)
, PRIMARY KEY (line_id)
, CONSTRAINT FK_LINE_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_LINE_SVTYPE FOREIGN KEY (service_type)
    REFERENCES sv_type(service_type) ON DELETE CASCADE
, CONSTRAINT UNIQ_LINE_DESC UNIQUE (city_id, description)
);

CREATE TABLE eel_noise_words
( id          INTEGER GENERATED ALWAYS AS IDENTITY
, noise_word  CHARACTER VARYING(75)  NOT NULL
, city_code   CHARACTER VARYING(3)
, PRIMARY KEY (id)
, CONSTRAINT FK_EELNW_CITY FOREIGN KEY (city_code)
    REFERENCES city(city_code) ON DELETE CASCADE
);

CREATE TABLE sp_segment
( segment_id   CHARACTER VARYING(25) NOT NULL
, city_id      CHARACTER VARYING(25) NOT NULL
, transport_id CHARACTER VARYING(25) NOT NULL
, PRIMARY KEY (segment_id)
, CONSTRAINT FK_SEG_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_SEG_TRANSTYPE FOREIGN KEY (transport_id)
    REFERENCES transport_type(transport_id) ON DELETE CASCADE
);

CREATE TABLE ti_traject
( traject_id   CHARACTER VARYING(25) NOT NULL
, city_id      CHARACTER VARYING(25) NOT NULL
, transport_id CHARACTER VARYING(25) NOT NULL
, PRIMARY KEY (traject_id)
, CONSTRAINT FK_TRAJECT_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_TRAJECT_TRANSPORT FOREIGN KEY (transport_id)
    REFERENCES transport_type (transport_id) ON DELETE CASCADE
);

CREATE TABLE station
( station_id    CHARACTER VARYING(75)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, city_id       CHARACTER VARYING(25)  NOT NULL
, node_id       CHARACTER VARYING(75)
, station_type  CHARACTER VARYING(75)  NOT NULL
, PRIMARY KEY (station_id)
, CONSTRAINT FK_STATION_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_STATION_TYPE FOREIGN KEY (station_type)
    REFERENCES station_type(station_type) ON DELETE CASCADE
);

CREATE TABLE landmark
( landmark_id   CHARACTER VARYING(75)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, city_id       CHARACTER VARYING(25)  NOT NULL
, PRIMARY KEY (landmark_id)
, CONSTRAINT FK_LANDMARK_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
);

-- Level 4
CREATE TABLE location
( location_id   CHARACTER VARYING(75) NOT NULL
, latitude      DECIMAL(10,7)
, longitude     DECIMAL(10,7)
, location_type CHARACTER VARYING(10) NOT NULL 
, localname     CHARACTER VARYING(255)
, PRIMARY KEY (location_id)
);

CREATE TABLE direction
( dir_id        CHARACTER VARYING(25)  NOT NULL
, city_id       CHARACTER VARYING(25)  NOT NULL
, description   CHARACTER VARYING(100) NOT NULL
, line_id       CHARACTER VARYING(25)  NOT NULL
, endpoint      CHARACTER VARYING(100) NOT NULL
, PRIMARY KEY (dir_id)
, CONSTRAINT FK_DIR_CITY FOREIGN KEY (city_id)
    REFERENCES city(city_id) ON DELETE CASCADE
, CONSTRAINT FK_DIR_LINE FOREIGN KEY (line_id)
    REFERENCES swwline(line_id) ON DELETE CASCADE
, CONSTRAINT UNIQ_DIR_DESC UNIQUE (city_id, description)
, CONSTRAINT UNIQ_LINE_END UNIQUE (line_id, endpoint)
);

CREATE TABLE ti_traject_stop
( traject_id    CHARACTER VARYING(25) NOT NULL
, station_id    CHARACTER VARYING(75) NOT NULL
, stop_sequence INTEGER     NOT NULL
, PRIMARY KEY (traject_id,stop_sequence)
, CONSTRAINT FK_STOP_STATION FOREIGN KEY (station_id)
    REFERENCES station (station_id) ON DELETE CASCADE
, CONSTRAINT FK_STOP_TRAJECT FOREIGN KEY (traject_id)
    REFERENCES ti_traject (traject_id) ON DELETE CASCADE
);

CREATE TABLE sp_seg_stop
( segment_id  CHARACTER VARYING(25)  NOT NULL
, seqno       INTEGER      NOT NULL
, station_id  CHARACTER VARYING(75)  NOT NULL
, PRIMARY KEY (segment_id, seqno)
, CONSTRAINT FK_SEGSTOP_STATION FOREIGN KEY (station_id)
    REFERENCES station(station_id) ON DELETE CASCADE
, CONSTRAINT FK_SEGSTOP_SEGMENT FOREIGN KEY (segment_id)
    REFERENCES sp_segment(segment_id) ON DELETE CASCADE
);

CREATE TABLE station_eel
( station_id   CHARACTER VARYING(75) NOT NULL
, eel           CHARACTER VARYING(1) NOT NULL
, PRIMARY KEY (station_id, eel)
, CONSTRAINT FK_STATEEL_STATION FOREIGN KEY (station_id)
    REFERENCES station(station_id) ON DELETE CASCADE
);

CREATE TABLE landmark_station
( landmark_id   CHARACTER VARYING(75)  NOT NULL
, station_id    CHARACTER VARYING(75)  NOT NULL
, PRIMARY KEY (landmark_id, station_id)
, CONSTRAINT FK_LMSTAT_LANDMARK FOREIGN KEY (landmark_id)
    REFERENCES landmark(landmark_id)  ON DELETE CASCADE
);

CREATE TABLE landmark_eel
( landmark_id   CHARACTER VARYING(75)  NOT NULL
, eel           CHAR(1)      NOT NULL
, PRIMARY KEY (landmark_id, eel)
, CONSTRAINT FK_LMEEL_LANDMARK FOREIGN KEY (landmark_id)
    REFERENCES landmark(landmark_id) ON DELETE CASCADE
);

-- Level 5
CREATE TABLE direction_override
( station_id      CHARACTER VARYING(75) NOT NULL
, dir_id          CHARACTER VARYING(25) NOT NULL
, override_dir_id CHARACTER VARYING(25) NOT NULL
, PRIMARY KEY (station_id,dir_id)
, CONSTRAINT FK_DIR_2_OVER_DIR FOREIGN KEY (override_dir_id)
    REFERENCES direction (dir_id) ON DELETE CASCADE
, CONSTRAINT FK_DIR_OVER_DIR FOREIGN KEY (dir_id)
    REFERENCES direction (dir_id) ON DELETE CASCADE
, CONSTRAINT FK_DIR_OVER_STAT FOREIGN KEY (station_id)
    REFERENCES station (station_id) ON DELETE CASCADE
);

CREATE TABLE transform_point
( pre_dir_id  CHARACTER VARYING(25)  NOT NULL
, station_id  CHARACTER VARYING(75)  NOT NULL
, post_dir_id CHARACTER VARYING(25)  NOT NULL
, PRIMARY KEY (pre_dir_id, station_id, post_dir_id)
, CONSTRAINT FK_TRFM_DIR_PRE FOREIGN KEY (pre_dir_id)
    REFERENCES direction(dir_id) ON DELETE CASCADE
, CONSTRAINT FK_TRFM_DIR_POST FOREIGN KEY (post_dir_id)
    REFERENCES direction(dir_id) ON DELETE CASCADE
, CONSTRAINT FK_TRFM_DIR_STATION
    FOREIGN KEY (station_id)  REFERENCES station(station_id)
		ON DELETE CASCADE
);

CREATE TABLE sp_seg_direction
( segment_id     CHARACTER VARYING(25)  NOT NULL
, dir_id         CHARACTER VARYING(25)  NOT NULL
, direction_type CHARACTER VARYING(3)   NOT NULL -- Domain: FWD, BCK.
, PRIMARY KEY (segment_id, dir_id, direction_type)
, CONSTRAINT FK_SEGDIR_DIR FOREIGN KEY (dir_id)
    REFERENCES direction(dir_id) ON DELETE CASCADE
, CONSTRAINT FK_SEGDIR_SEGMENT FOREIGN KEY (segment_id)
    REFERENCES sp_segment(segment_id) ON DELETE CASCADE
, CONSTRAINT FK_SEGDIR_DIR_TYPE FOREIGN KEY (direction_type)
    REFERENCES direction_type(direction_type) ON DELETE CASCADE
);

CREATE TABLE ti_traject_dir
( traject_id CHARACTER VARYING(25) NOT NULL
, fwd_dir_id CHARACTER VARYING(25) NOT NULL
, PRIMARY KEY (traject_id, fwd_dir_id)
, CONSTRAINT FK_TRJDIR_DIRECTION FOREIGN KEY (fwd_dir_id)
    REFERENCES direction (dir_id) ON DELETE CASCADE
, CONSTRAINT FK_TRJDIR_TRAJECT FOREIGN KEY (traject_id)
    REFERENCES ti_traject (traject_id) ON DELETE CASCADE
);

-- End of file
