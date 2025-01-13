--
-- Created: 12-12-2009.
--
-- Script to create sww (SubWayWorld) database schema and users.
--
-- Run this script as root.
--
-- mysql -u root -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- Version Date       Who  Remark
-- 1.0     12-12-09   MVD  Initial version
-- 1.1     2025-01-13 MVD  Update for mysql version 8
--                         charset utf8 obsolete. Change to:
--                         - utf8mb4 [supports BMP and supplementary characters].
--                         - collate utf8mb4_0900_ai_ci
--                           0900 refers to the Unicode Collation Algorithm version.
--                           ai refers accent insensitivity when sorting. 
--                           ci refers to case insensitivity when sorting.

CREATE SCHEMA IF NOT EXISTS sww2 
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

SHOW WARNINGS;

-- The @'localhost' means that sww_owner can only connect from localhost.
-- Use @'%'to allow sww_owner to connect from any host.
CREATE USER 'sww_owner'@'%' IDENTIFIED WITH caching_sha2_password BY 'XXX';

SHOW WARNINGS;

GRANT 
  SELECT, INSERT, UPDATE, DELETE, LOCK TABLES
, EXECUTE
, GRANT OPTION
, DROP
, CREATE, CREATE TEMPORARY TABLES, CREATE VIEW, INDEX, CREATE ROUTINE
, ALTER, ALTER ROUTINE, SHOW VIEW
, REFERENCES
  ON sww2.*
  TO 'sww_owner'@'%'
;

SHOW WARNINGS;

CREATE USER 'sww_reader'@'%' IDENTIFIED WITH caching_sha2_password BY 'sww_reader';

SHOW WARNINGS;

GRANT 
  SELECT
  ON sww2.*
  TO 'sww_reader'@'%'
;

SHOW WARNINGS;
