--
-- Created: 26-06-2012
--
-- Script to create sww (SubWayWorld) rs_XXX tables.
--
-- Run this script as sww_owner.
-- mysql -u sww_owner -t -vvv -p < scriptname.sql > scriptname_log.txt
--
--
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP VIEW IF EXISTS rs_city;
DROP VIEW IF EXISTS rs_network_station;
DROP VIEW IF EXISTS rs_display_station;
DROP VIEW IF EXISTS rs_landmark;
DROP VIEW IF EXISTS rs_landmark_station;
DROP VIEW IF EXISTS rs_direction;
DROP VIEW IF EXISTS seg_stop_metro;
DROP VIEW IF EXISTS transform_metro;
DROP VIEW IF EXISTS rs_station_eel;
DROP VIEW IF EXISTS rs_landmark_eel;
DROP VIEW IF EXISTS rs_seg_directions;
DROP VIEW IF EXISTS rs_mnn_nodes;
DROP VIEW IF EXISTS rs_mnn_segments;
DROP VIEW IF EXISTS rs_city_publish;
DROP VIEW IF EXISTS rs_continent_publish;
DROP VIEW IF EXISTS rs_country_publish;
DROP VIEW IF EXISTS rs_locations;

DROP TABLE IF EXISTS rs_city;
CREATE TABLE rs_city
AS
SELECT ct.city_id       as city_id
     , ct.city_code     as city_code
FROM   city ct
WHERE  ct.has_line_info = 'Y'
;
CREATE UNIQUE INDEX idx_rs_city ON rs_city(city_id);
CREATE UNIQUE INDEX idx_rs_city_code ON rs_city(city_code);
ANALYZE TABLE rs_city;

DROP TABLE IF EXISTS rs_network_station;
CREATE TABLE rs_network_station
AS
SELECT s.station_id
     , s.description
     , city_id
     , loc.localname as local_name
     , loc.latitude
     , loc.longitude
FROM   station s 
         LEFT OUTER JOIN location loc ON s.station_id = loc.location_id
;
CREATE UNIQUE INDEX idx_rs_network_station ON rs_network_station(station_id);
CREATE INDEX idx_rs_network_station_2 ON rs_network_station(city_id);

DROP TABLE IF EXISTS rs_display_station;
CREATE TABLE rs_display_station
AS
SELECT s.station_id
     , s.description
     , city_id
     , loc.localname as local_name
     , loc.latitude
     , loc.longitude
FROM   station s 
         LEFT OUTER JOIN location loc ON s.station_id = loc.location_id
;
CREATE UNIQUE INDEX idx_rs_display_station ON rs_display_station(station_id);
CREATE INDEX idx_rs_display_station_2 ON rs_display_station(city_id);
ANALYZE TABLE rs_display_station;

DROP TABLE IF EXISTS rs_landmark;
CREATE TABLE rs_landmark
AS
SELECT landmark_id
     , description
     , city_id
     , loc.latitude
     , loc.longitude
     , loc.localname as local_name
FROM   landmark m
         LEFT OUTER JOIN location loc ON m.landmark_id = loc.location_id
WHERE  EXISTS (SELECT 'x'
               FROM   landmark_station s
							 WHERE  s.landmark_id = m.landmark_id)
;
CREATE UNIQUE INDEX idx_rs_landmark ON rs_landmark(landmark_id);
CREATE INDEX idx_rs_landmark_2 ON rs_landmark(city_id);
ANALYZE TABLE rs_landmark;

DROP TABLE IF EXISTS rs_landmark_station;
CREATE TABLE rs_landmark_station
AS
SELECT m.landmark_id
     , m.description
     , loc.latitude
     , loc.longitude
     , loc.localname as local_name
     , m.city_id
     , ls.station_id
FROM   landmark_station ls  JOIN landmark m ON ls.landmark_id = m.landmark_id
       LEFT OUTER JOIN location loc ON m.landmark_id = loc.location_id
;
CREATE UNIQUE INDEX idx_rs_landmark_station ON rs_landmark_station(landmark_id, station_id);
CREATE INDEX idx_rs_landmark_station_2 ON rs_landmark_station(city_id);
ANALYZE TABLE rs_landmark_station;

DROP TABLE IF EXISTS rs_direction;
CREATE TABLE rs_direction
AS
SELECT d.dir_id
     , d.city_id
     , d.description
		 , s.description line
		 , d.endpoint
     , s.service_type
		 , s.remark
FROM   direction d join swwline s on d.line_id = s.line_id
;
CREATE UNIQUE INDEX idx_rs_direction ON rs_direction(dir_id);
CREATE INDEX idx_rs_direction_2 ON rs_direction(city_id);
ANALYZE TABLE rs_direction;


DROP TABLE IF EXISTS seg_stop_metro;
CREATE TABLE seg_stop_metro
AS
SELECT seg.city_id
     , stp.segment_id
     , stp.seqno
     , stp.station_id
FROM   sp_seg_stop stp join sp_segment seg on stp.segment_id = seg.segment_id
;
CREATE INDEX idx_seg_stop_metro ON seg_stop_metro(city_id);
ANALYZE TABLE seg_stop_metro;

DROP TABLE IF EXISTS transform_metro;
CREATE TABLE transform_metro
AS
SELECT pre.city_id      as city_id
     , tp.station_id
     , tp.pre_dir_id
     , pre.description  as pre_description
     , tp.post_dir_id
     , post.description as post_description
FROM   transform_point tp join direction pre on tp.pre_dir_id = pre.dir_id
       join direction post on tp.post_dir_id = post.dir_id
			 join station st on tp.station_id = st.station_id
;
CREATE INDEX idx_transform_metro ON transform_metro(city_id);
ANALYZE TABLE transform_metro;

DROP TABLE IF EXISTS rs_station_eel;
CREATE TABLE rs_station_eel
AS
SELECT s.station_id
     , s.description
		 , s.city_id
		 , loc.latitude
		 , loc.longitude
		 , loc.localname as local_name
		 , se.eel
FROM station_eel se join station s on se.station_id = s.station_id
       LEFT OUTER JOIN location loc ON s.station_id = loc.location_id
;
CREATE INDEX idx_rs_station_eel ON rs_station_eel(city_id);
CREATE INDEX idx_rs_station_eel_2 ON rs_station_eel(city_id, eel);
ANALYZE TABLE rs_station_eel;

DROP TABLE IF EXISTS rs_landmark_eel;
CREATE TABLE rs_landmark_eel
AS
SELECT l.landmark_id
     , l.description
		 , l.city_id
		 , loc.latitude
		 , loc.longitude
     , loc.localname as local_name
		 , le.eel
FROM landmark_eel le join landmark l on le.landmark_id = l.landmark_id
       LEFT OUTER JOIN location loc ON l.landmark_id = loc.location_id
;
CREATE INDEX idx_rs_landmark_eel ON rs_landmark_eel(city_id);
CREATE INDEX idx_rs_landmark_eel_2 ON rs_landmark_eel(city_id, eel);
ANALYZE TABLE rs_landmark_eel;

DROP TABLE IF EXISTS rs_seg_directions;
CREATE TABLE rs_seg_directions
AS
SELECT sd.segment_id
     , d.description
		 , sd.direction_type
		 , d.city_id
FROM   sp_seg_direction sd join direction d on sd.dir_id = d.dir_id
;
CREATE INDEX idx_rs_seg_directions ON rs_seg_directions(city_id);
ANALYZE TABLE rs_seg_directions;

DROP TABLE IF EXISTS rs_mnn_nodes;
CREATE TABLE rs_mnn_nodes
AS
SELECT city_id
     , node_id
		 , station_id
FROM station
WHERE node_id <> station_id
;
CREATE INDEX idx_rs_mnn_nodes ON rs_mnn_nodes(city_id);
ANALYZE TABLE rs_mnn_nodes;

DROP TABLE IF EXISTS rs_mnn_segments;
CREATE TABLE rs_mnn_segments
AS
SELECT r.city_id
     , r.node_id
     , r.station_id   from_stat
     , r2.station_id  to_stat
FROM rs_mnn_nodes r JOIN rs_mnn_nodes r2 ON
      r.node_id = r2.node_id and r.city_id = r2.city_id
WHERE r.station_id <> r2.station_id
;
CREATE INDEX idx_rs_mnn_segments ON rs_mnn_segments(city_id);
ANALYZE TABLE rs_mnn_segments;

DROP TABLE IF EXISTS rs_city_publish;
CREATE TABLE rs_city_publish
AS
SELECT ct.city_id       as city_id
     , ct.city_code     as city_code
     , ct.description   as city_desc
     , loc.latitude
     , loc.longitude
		 , loc.localname as local_name
     , ct.continent_id  as continent_id
     , con.description  as continent_desc
     , ct.country_id    as country_id
     , ctry.description as country_desc
     , null             as info_timestamp
     , null             as info_status
     , null             as info_message
FROM   city ct
        LEFT OUTER JOIN location loc ON ct.city_id = loc.location_id
            JOIN continent con ON ct.continent_id = con.continent_id
              JOIN country ctry ON ct.country_id = ctry.country_id
WHERE  ct.has_line_info = 'Y'
AND    NOT EXISTS (select * from city_status stat
                   where stat.city_id = ct.city_id
                   and   stat.status IN ('PUBLISH_UPDATE', 'PUBLISH_NEW')
                   AND   stat.timestamp >= 
									       date_sub(curdate(), INTERVAL 10 DAY) )
UNION ALL
SELECT ct.city_id       as city_id
     , ct.city_code     as city_code
     , ct.description   as city_desc
     , loc.latitude
     , loc.longitude
		 , loc.localname as local_name
     , ct.continent_id  as continent_id
     , con.description  as continent_desc
     , ct.country_id    as country_id
     , ctry.description as country_desc
     , stat.timestamp   as info_timestamp
     , stat.status      as info_status
     , SUBSTR(stat.message, 1, 250) as info_message
FROM   city ct
        LEFT OUTER JOIN location loc ON ct.city_id = loc.location_id
          JOIN city_status stat ON ct.city_id = stat.city_id
            JOIN continent con ON ct.continent_id = con.continent_id
              JOIN country ctry ON ct.country_id = ctry.country_id
WHERE  ct.has_line_info = 'Y'
AND    stat.status IN ('PUBLISH_UPDATE', 'PUBLISH_NEW')
AND    stat.timestamp >= date_sub(curdate(), INTERVAL 10 DAY)
;
CREATE INDEX idx_rs_city_publish ON rs_city_publish(city_id);
CREATE INDEX idx_rs_city_publish_2 ON rs_city_publish(continent_id);
CREATE INDEX idx_rs_city_publish_3 ON rs_city_publish(country_id);
ANALYZE TABLE rs_city_publish;

DROP TABLE IF EXISTS rs_continent_publish;
CREATE TABLE rs_continent_publish
AS
SELECT continent_id
     , continent_desc
     , info_status
     , MAX(info_timestamp) info_timestamp
     , COUNT(DISTINCT city_id) info_message
FROM   rs_city_publish
GROUP BY continent_id, continent_desc, info_status
;
CREATE INDEX idx_rs_continent_publish ON rs_continent_publish(continent_id);
ANALYZE TABLE rs_continent_publish;

DROP TABLE IF EXISTS rs_country_publish;
CREATE TABLE rs_country_publish
AS
SELECT country_id
     , country_desc
     , continent_id
     , continent_desc
     , info_status
     , MAX(info_timestamp) info_timestamp
     , COUNT(DISTINCT city_id) info_message
FROM   rs_city_publish
GROUP BY country_id, country_desc, continent_id, continent_desc, info_status
;
CREATE INDEX idx_rs_country_publish ON rs_country_publish(country_id);
ANALYZE TABLE rs_country_publish;

DROP TABLE IF EXISTS rs_locations;
CREATE TABLE rs_locations
AS
select loc.location_id
     , stat.description
		 , loc.localname as local_name
		 , loc.latitude
		 , loc.longitude
		 , loc.type
from   location loc
         join station stat on loc.location_id = stat.station_id
where  upper(loc.type) = 'STATION'
UNION ALL
select loc.location_id
     , ct.description
		 , loc.localname as local_name
		 , loc.latitude
		 , loc.longitude
		 , loc.type
from   location loc
         join city ct on loc.location_id = ct.city_id
where  upper(loc.type) = 'CITY'
UNION ALL
select loc.location_id
     , land.description
		 , loc.localname as local_name
		 , loc.latitude
		 , loc.longitude
		 , loc.type
from   location loc
         join landmark land on loc.location_id = land.landmark_id
where  upper(loc.type) = 'LANDMARK'
;
CREATE UNIQUE INDEX idx_rs_locations ON rs_locations(location_id);
ANALYZE TABLE rs_locations;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

