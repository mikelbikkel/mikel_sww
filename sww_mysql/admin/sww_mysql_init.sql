--
-- Created: 12-12-2009.
--
-- Script to create sww (SubWayWorld) database schema and users.
--
-- Run this script as root.
--
-- mysql -u root -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- TODO: schema, owner user, access users (r, rw, .....)
-- Version Date     Who  Remark
-- 1.0     12-12-09 MVD  Initial version
--

CREATE SCHEMA IF NOT EXISTS `sww2` 
  DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

SHOW WARNINGS;

-- The @'localhost' means that sww_owner can only connect from localhost.
-- Use @'%'to allow sww_owner to connect from any host.
CREATE USER 'sww_owner'@'localhost' IDENTIFIED BY 'XXX';

SHOW WARNINGS;

GRANT 
  SELECT, INSERT, UPDATE, DELETE, LOCK TABLES
, EXECUTE
, GRANT OPTION
, DROP
, CREATE, CREATE TEMPORARY TABLES, CREATE VIEW, INDEX, CREATE ROUTINE
, ALTER, ALTER ROUTINE, SHOW VIEW
  ON sww2.*
  TO 'sww_owner'@'localhost'
;

SHOW WARNINGS;

CREATE USER 'sww_reader'@'localhost' IDENTIFIED BY 'sww_reader';

SHOW WARNINGS;

GRANT 
  SELECT
  ON sww2.*
  TO 'sww_reader'@'localhost'
;

SHOW WARNINGS;
