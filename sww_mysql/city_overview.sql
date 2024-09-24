delimiter $$

DROP VIEW IF EXISTS city_overview $$
CREATE VIEW city_overview 
AS 
	select 
		
		 city_id AS city_id
		,city_code AS city_code
		,description AS description
		
		,(select 
			count(station.station_id) 
			from station 
			where (station.city_id = c.city_id)
		 ) AS stationcount
	
		,(select 
			count(landmark.landmark_id) 
			from landmark 
			where (landmark.city_id = c.city_id)
		 ) AS landmarkcount
		 
		,(select 
			count(distinct station.station_id) 
			from (station left join location on((station.station_id = location.location_id ))) 
			where ((station.city_id = c.city_id) and (location.latitude is not null)) /* only need to check latitude or longitude */
		 ) AS stationlocs
		 
		,(select 
			count(distinct landmark.landmark_id) 
			from (landmark left join location on((landmark.landmark_id = location.location_id ))) 
			where ((landmark.city_id = c.city_id) and (location.latitude is not null)) /* only need to check latitude or longitude */
		 ) AS landmarklocs
		 
		,(select 
			s.status 
			from (city_status s left join city ct on(s.city_id = ct.city_id ))
			where ((ct.city_id = c.city_id) and (s.status <> 'GENERAL_NOTIFICATION')) 
			order by s.timestamp desc 
			limit 1
		 ) AS lastCriticalState

	from 
		city c

$$


