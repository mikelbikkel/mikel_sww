DROP VIEW IF EXISTS rest_cities;
CREATE VIEW rest_cities
AS
SELECT ct.city_id       as city_id
     , ct.city_code     as city_code
     , ct.description   as city_desc
     , loc.latitude
     , loc.longitude
     , ct.continent_id  as continent_id
     , con.description  as continent_desc
     , ct.country_id    as country_id
     , ctry.description as country_desc
FROM   city ct
        LEFT OUTER JOIN location loc ON ct.city_id = loc.location_id
          JOIN continent con ON ct.continent_id = con.continent_id
            JOIN country ctry ON ct.country_id = ctry.country_id
WHERE  ct.has_line_info = 'Y'
;

