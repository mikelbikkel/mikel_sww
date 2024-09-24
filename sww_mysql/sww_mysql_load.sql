--
-- Created: 12-12-2009.
--
-- Script to populate the SubWayWorld tables.
--
-- Run this script as sww_owner.
-- mysql -u sww_owner -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- Version Date     Who  Remark
-- 1.0     12-12-09 MVD  Initial version
--
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

START TRANSACTION;

DELETE FROM sv_type;
DELETE FROM city;
DELETE FROM country;
DELETE FROM transport_type;
DELETE FROM continent;
COMMIT;

START TRANSACTION;

INSERT INTO sv_type (service_type)
VALUES ('REGULAR')
;

INSERT INTO sv_type (service_type)
VALUES ('PARTIAL')
;

INSERT INTO sv_type (service_type)
VALUES ('EXTENDED')
;

INSERT INTO continent (continent_id, description)
VALUES ('AFRICA', 'Africa')
;

INSERT INTO continent (continent_id, description)
VALUES ('AMERICA', 'America')
;

INSERT INTO continent (continent_id, description)
VALUES ('ASIA', 'Asia')
;

INSERT INTO continent (continent_id, description)
VALUES ('EUROPE', 'Europe')
;

INSERT INTO continent (continent_id, description)
VALUES ('OCEANIA', 'Oceania')
;

INSERT INTO transport_type (transport_id, description)
VALUES ('METRO', 'Metro')
;

INSERT INTO transport_type (transport_id, description)
VALUES ('TRAM', 'Tram')
;

INSERT INTO country (country_id, description) VALUES('ALGERIA','Algeria');
INSERT INTO country (country_id, description) VALUES('ARGENTINA','Argentina');
INSERT INTO country (country_id, description) VALUES('ARMENIA','Armenia');
INSERT INTO country (country_id, description) VALUES('AUSTRALIA','Australia');
INSERT INTO country (country_id, description) VALUES('AUSTRIA','Austria');
INSERT INTO country (country_id, description) VALUES('AZERBAIJAN','Azerbaijan');
INSERT INTO country (country_id, description) VALUES('BELARUS','Belarus');
INSERT INTO country (country_id, description) VALUES('BELGIUM','Belgium');
INSERT INTO country (country_id, description) VALUES('BRAZIL','Brazil');
INSERT INTO country (country_id, description) VALUES('BULGARIA','Bulgaria');
INSERT INTO country (country_id, description) VALUES('CANADA','Canada');
INSERT INTO country (country_id, description) VALUES('CHILE','Chile');
INSERT INTO country (country_id, description) VALUES('CHINA','China');
INSERT INTO country (country_id, description) VALUES('COLOMBIA','Colombia');
INSERT INTO country (country_id, description) VALUES('CZECH_REPUBLIC','Czech Republic');
INSERT INTO country (country_id, description) VALUES('DENMARK','Denmark');
INSERT INTO country (country_id, description) VALUES('DOMINICAN_REPUBLIC','Dominican Republic');
INSERT INTO country (country_id, description) VALUES('EGYPT','Egypt');
INSERT INTO country (country_id, description) VALUES('FINLAND','Finland');
INSERT INTO country (country_id, description) VALUES('FRANCE','France');
INSERT INTO country (country_id, description) VALUES('GEORGIA','Georgia');
INSERT INTO country (country_id, description) VALUES('GERMANY','Germany');
INSERT INTO country (country_id, description) VALUES('GREECE','Greece');
INSERT INTO country (country_id, description) VALUES('HUNGARY','Hungary');
INSERT INTO country (country_id, description) VALUES('INDIA','India');
INSERT INTO country (country_id, description) VALUES('IRAN','Iran');
INSERT INTO country (country_id, description) VALUES('ISRAEL','Israel');
INSERT INTO country (country_id, description) VALUES('ITALY','Italy');
INSERT INTO country (country_id, description) VALUES('JAPAN','Japan');
INSERT INTO country (country_id, description) VALUES('MALAYSIA','Malaysia');
INSERT INTO country (country_id, description) VALUES('MEXICO','Mexico');
INSERT INTO country (country_id, description) VALUES('NETHERLANDS','Netherlands');
INSERT INTO country (country_id, description) VALUES('NORTH_KOREA','North Korea');
INSERT INTO country (country_id, description) VALUES('NORWAY','Norway');
INSERT INTO country (country_id, description) VALUES('PERU','Peru');
INSERT INTO country (country_id, description) VALUES('PHILIPPINES','Philippines');
INSERT INTO country (country_id, description) VALUES('POLAND','Poland');
INSERT INTO country (country_id, description) VALUES('PORTUGAL','Portugal');
INSERT INTO country (country_id, description) VALUES('PUERTO_RICO','Puerto Rico');
INSERT INTO country (country_id, description) VALUES('ROMANIA','Romania');
INSERT INTO country (country_id, description) VALUES('RUSSIA','Russia');
INSERT INTO country (country_id, description) VALUES('SINGAPORE','Singapore');
INSERT INTO country (country_id, description) VALUES('SOUTH_KOREA','South Korea');
INSERT INTO country (country_id, description) VALUES('SPAIN','Spain');
INSERT INTO country (country_id, description) VALUES('SWEDEN','Sweden');
INSERT INTO country (country_id, description) VALUES('SWITZERLAND','Switzerland');
INSERT INTO country (country_id, description) VALUES('TAIWAN','Taiwan');
INSERT INTO country (country_id, description) VALUES('THAILAND','Thailand');
INSERT INTO country (country_id, description) VALUES('TURKEY','Turkey');
INSERT INTO country (country_id, description) VALUES('UKRAINE','Ukraine');
INSERT INTO country (country_id, description) VALUES('UNITED_ARAB_EMIRATES','United Arab Emirates');
INSERT INTO country (country_id, description) VALUES('UNITED_KINGDOM','United Kingdom');
INSERT INTO country (country_id, description) VALUES('USA','USA');
INSERT INTO country (country_id, description) VALUES('UZBEKISTAN','Uzbekistan');
INSERT INTO country (country_id, description) VALUES('VENEZUELA','Venezuela');

INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ADANA', 'ADA', 'Adana', 'TURKEY', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ALGIERS', 'ALG', 'Algiers', 'ALGERIA', 'AFRICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('AMSTERDAM', 'AMS', 'Amsterdam', 'NETHERLANDS', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ANKARA', 'ANK', 'Ankara', 'TURKEY', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ATHENS', 'ANT', 'Athens', 'GREECE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ANTWERP', 'ANW', 'Antwerp', 'BELGIUM', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ATLANTA', 'ATL', 'Atlanta', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BALTIMORE', 'BAL', 'Baltimore', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BARCELONA', 'BAR', 'Barcelona', 'SPAIN', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BELO_HORIZONTE', 'BEL', 'Belo Horizonte', 'BRAZIL', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BEIJING', 'BIJ', 'Beijing', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BAKU', 'BKU', 'Baku', 'AZERBAIJAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BANGKOK', 'BNG', 'Bangkok', 'THAILAND', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BRASILIA', 'BRA', 'Brasilia', 'BRAZIL', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BRUSSELS', 'BRS', 'Brussels', 'BELGIUM', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BUENOS_AIRES', 'BUE', 'Buenos Aires', 'ARGENTINA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BUFFALO', 'BUF', 'Buffalo', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BURSA', 'BUR', 'Bursa', 'TURKEY', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('BUSAN', 'BUS', 'Busan', 'SOUTH_KOREA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CAIRO', 'CAI', 'Cairo', 'EGYPT', 'AFRICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CARACAS', 'CAR', 'Caracas', 'VENEZUELA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CATANIA', 'CAT', 'Catania', 'ITALY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CHICAGO', 'CCG', 'Chicago', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CHANGCHUN', 'CHA', 'Changchun', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CHENNAI', 'CHE', 'Chennai', 'INDIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CHIBA', 'CHI', 'Chiba', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CHONGQING', 'CHQ', 'Chongqing', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('CLEVELAND', 'CLV', 'Cleveland', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('COLOGNE', 'COL', 'Cologne', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('COPENHAGEN', 'COP', 'Copenhagen', 'DENMARK', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DAEGU', 'DAE', 'Daegu', 'SOUTH_KOREA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DALIAN', 'DAL', 'Dalian', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DELHI', 'DEL', 'Delhi', 'INDIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DETROIT', 'DET', 'Detroit', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DAEJEON', 'DJN', 'Daejeon', 'SOUTH_KOREA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DNEPROPETROVSK', 'DNE', 'Dnepropetrovsk', 'UKRAINE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('THE_HAGUE', 'DNH', 'The Hague', 'NETHERLANDS', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DORTMUND', 'DOR', 'Dortmund', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DUBAI', 'DUB', 'Dubai', 'UNITED_ARAB_EMIRATES', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DUESSELDORF', 'DUE', 'Duesseldorf', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('DUISBURG', 'DUI', 'Duisburg', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('EDMONTON', 'EDM', 'Edmonton', 'CANADA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ESSEN', 'ESN', 'Essen', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('FUKUOKA', 'FKK', 'Fukuoka', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('FRANKFURT', 'FRA', 'Frankfurt', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('GUADALAJARA', 'GDL', 'Guadalajara', 'MEXICO', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('GENOA', 'GEN', 'Genoa', 'ITALY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('GLASGOW', 'GLS', 'Glasgow', 'UNITED_KINGDOM', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('GELSENKIRCHEN', 'GSK', 'Gelsenkirchen', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('GWANGJU', 'GWG', 'Gwangju', 'SOUTH_KOREA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('GUANGZHOU', 'GZH', 'Guangzhou', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('HAIFA', 'HAI', 'Haifa', 'ISRAEL', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('HANOVER', 'HAN', 'Hanover', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('HELSINKI', 'HEL', 'Helsinki', 'FINLAND', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('HAMBURG', 'HMB', 'Hamburg', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('HONG_KONG', 'HNK', 'Hong Kong', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('HIROSHIMA', 'HRO', 'Hiroshima', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('INCHEON', 'INC', 'Incheon', 'SOUTH_KOREA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ISTANBUL', 'IST', 'Istanbul', 'TURKEY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('IZMIR', 'IZM', 'Izmir', 'TURKEY', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('JACKSONVILLE', 'JCK', 'Jacksonville', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KAMAKURA', 'KAM', 'Kamakura', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KAOHSIUNG', 'KAO', 'Kaohsiung', 'TAIWAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KAZAN', 'KAZ', 'Kazan', 'RUSSIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KITAKYUSHU', 'KIT', 'Kitakyushu', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KIEV', 'KIV', 'Kiev', 'UKRAINE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KOLKATA', 'KLK', 'Kolkata', 'INDIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KUALA_LUMPUR', 'KLL', 'Kuala Lumpur', 'MALAYSIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KOBE', 'KOB', 'Kobe', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KHARKOV', 'KRK', 'Kharkov', 'UKRAINE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KRYVYI_RIH', 'KRR', 'Kryvyi Rih', 'UKRAINE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('KYOTO', 'KYO', 'Kyoto', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LOS_ANGELES', 'LAS', 'Los Angeles', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LAUSANNE', 'LAU', 'Lausanne', 'SWITZERLAND', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LILLE', 'LIL', 'Lille', 'FRANCE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LISBON', 'LIS', 'Lisbon', 'PORTUGAL', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LIMA', 'LMA', 'Lima', 'PERU', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LONDON', 'LND', 'London', 'UNITED_KINGDOM', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LUDWIGSHAFEN', 'LUD', 'Ludwigshafen', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LAS_VEGAS', 'LVG', 'Las Vegas', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('LYON', 'LYN', 'Lyon', 'FRANCE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MANILA', 'MAN', 'Manila', 'PHILIPPINES', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MARSEILLE', 'MAR', 'Marseille', 'FRANCE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MARACAIBO', 'MCB', 'Maracaibo', 'VENEZUELA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MADRID', 'MDR', 'Madrid', 'SPAIN', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MEDELLIN', 'MED', 'Medellin', 'COLOMBIA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MIAMI', 'MIA', 'Miami', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MILAN', 'MIL', 'Milan', 'ITALY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MINSK', 'MNK', 'Minsk', 'BELARUS', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MONTREAL', 'MNL', 'Montreal', 'CANADA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MONTERREY', 'MNT', 'Monterrey', 'MEXICO', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MOSCOW', 'MSW', 'Moscow', 'RUSSIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MULHEIM', 'MUL', 'Mulheim', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MUMBAI', 'MUM', 'Mumbai', 'INDIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MUNICH', 'MUN', 'Munich', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('MEXICO_CITY', 'MXC', 'Mexico City', 'MEXICO', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NAGOYA', 'NAG', 'Nagoya', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NAHA', 'NAH', 'Naha', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NANJING', 'NAN', 'Nanjing', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NAPLES', 'NAP', 'Naples', 'ITALY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NOVOSIBIRSK', 'NOV', 'Novosibirsk', 'RUSSIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NUREMBERG', 'NUR', 'Nuremberg', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NEWARK', 'NWA', 'Newark', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NEWCASTLE', 'NWC', 'Newcastle', 'UNITED_KINGDOM', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NEW_YORK', 'NYC', 'New York', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('NIZHNY_NOVGOROD', 'NZN', 'Nizhny Novgorod', 'RUSSIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('OSAKA', 'OSA', 'Osaka', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('OSLO', 'OSL', 'Oslo', 'NORWAY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PALMA_DE_MALLORCA', 'PDM', 'Palma de Mallorca', 'SPAIN', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PERUGIA', 'PER', 'Perugia', 'ITALY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PHILADELPHIA', 'PHI', 'Philadelphia', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PITTSBURGH', 'PIT', 'Pittsburgh', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('POZNAN', 'POZ', 'Poznan', 'POLAND', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PORTO_ALEGRE', 'PRA', 'Porto Alegre', 'BRAZIL', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PRAGUE', 'PRG', 'Prague', 'CZECH_REPUBLIC', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PARIS', 'PRS', 'Paris', 'FRANCE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('OPORTO', 'PRT', 'Oporto', 'PORTUGAL', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('PYONGYANG', 'PYY', 'Pyongyang', 'NORTH_KOREA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('RIO_DE_JANEIRO', 'RDJ', 'Rio de Janeiro', 'BRAZIL', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ROTTERDAM', 'RDM', 'Rotterdam', 'NETHERLANDS', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('RECIFE', 'REC', 'Recife', 'BRAZIL', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('RENNES', 'REN', 'Rennes', 'FRANCE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ROME', 'ROM', 'Rome', 'ITALY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('ROUEN', 'ROU', 'Rouen', 'FRANCE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SAMARA', 'SAM', 'Samara', 'RUSSIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SAPPORO', 'SAP', 'Sapporo', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SANTO_DOMINGO', 'SDO', 'Santo Domingo', 'DOMINICAN_REPUBLIC', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SEATTLE', 'SEA', 'Seattle', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SENDAI', 'SEN', 'Sendai', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SEOUL', 'SEO', 'Seoul', 'SOUTH_KOREA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SEVILLE', 'SEV', 'Seville', 'SPAIN', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SAN_FRANCISCO', 'SFR', 'San Francisco', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SHANGHAI', 'SHA', 'Shanghai', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SHENZHEN', 'SHE', 'Shenzhen', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SAN_JUAN', 'SJU', 'San Juan', 'PUERTO_RICO', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SINGAPORE', 'SNG', 'Singapore', 'SINGAPORE', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SANTIAGO', 'SNT', 'Santiago', 'CHILE', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SOFIA', 'SOF', 'Sofia', 'BULGARIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SAO_PAULO', 'SPA', 'Sao Paulo', 'BRAZIL', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('STOCKHOLM', 'STK', 'Stockholm', 'SWEDEN', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SAINT_LOUIS', 'STL', 'Saint Louis', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SAINT_PETERSBURG', 'STP', 'Saint Petersburg', 'RUSSIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('STUTTGART', 'STT', 'Stuttgart', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('SYDNEY', 'SYD', 'Sydney', 'AUSTRALIA', 'OCEANIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TAIPEI', 'TAI', 'Taipei', 'TAIWAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TAMA', 'TAM', 'Tama', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TASHKENT', 'TAS', 'Tashkent', 'UZBEKISTAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TBILISI', 'TBL', 'Tbilisi', 'GEORGIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TEHRAN', 'THR', 'Tehran', 'IRAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TIANJIN', 'TIA', 'Tianjin', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TOKYO', 'TKY', 'Tokyo', 'JAPAN', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TORONTO', 'TOR', 'Toronto', 'CANADA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TOULOUSE', 'TOU', 'Toulouse', 'FRANCE', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('TURIN', 'TUR', 'Turin', 'ITALY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('VALPARAISO', 'VAL', 'Valparaiso', 'CHILE', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('VANCOUVER', 'VAN', 'Vancouver', 'CANADA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('VALENCIA_SP', 'VAS', 'Valencia (Spain)', 'SPAIN', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('VALENCIA_VE', 'VAV', 'Valencia (Venezuela)', 'VENEZUELA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('VIENNA', 'VIE', 'Vienna', 'AUSTRIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('VOLGOGRAD', 'VLG', 'Volgograd', 'RUSSIA', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('WASHINGTON', 'WAS', 'Washington', 'USA', 'AMERICA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('WARSAW', 'WRS', 'Warsaw', 'POLAND', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('WUHAN', 'WUH', 'Wuhan', 'CHINA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('WUPPERTAL', 'WUP', 'Wuppertal', 'GERMANY', 'EUROPE');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('YEKATERINBURG', 'YEK', 'Yekaterinburg', 'RUSSIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('YEREVAN', 'YER', 'Yerevan', 'ARMENIA', 'ASIA');
INSERT INTO city (city_id, city_code, description, country_id, continent_id) VALUES('YOKOHAMA', 'YOK', 'Yokohama', 'JAPAN', 'ASIA');

INSERT into city (city_id, city_code, description, continent_id, country_id, has_line_info)
VALUES ( 'BUCHAREST','BUC','Bucharest','EUROPE','ROMANIA','N' );

INSERT into city (city_id, city_code, description, continent_id, country_id, has_line_info)
VALUES ( 'BERLIN','BER','Berlin','EUROPE','GERMANY','N' );

INSERT into city (city_id, city_code, description, continent_id, country_id, has_line_info)
VALUES ( 'BUDAPEST','BUD','Budapest','EUROPE','HUNGARY','N' );

COMMIT;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

