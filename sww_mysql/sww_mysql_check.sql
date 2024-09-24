--
-- Script to check sww (SubWayWorld) database content.
--
-- Run this script as sww_owner.
-- mysql -u sww_owner -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- Version Date     Who  Remark
-- 1.0     24-10-10 MVD  Initial version
--
SELECT s.* 
FROM   station s JOIN city c ON s.city_id = c.city_id
WHERE  substr(s.station_id, 1, 3) <> c.city_code
;

SELECT s.*
FROM   station s
WHERE NOT EXISTS (select 'x'
                  FROM   station_eel se
									WHERE  s.station_id = se.station_id)
;

SELECT lm.*
FROM   landmark lm
WHERE NOT EXISTS (select 'x'
                  FROM   landmark_eel lme
									WHERE  lm.landmark_id = lme.landmark_id)
;

