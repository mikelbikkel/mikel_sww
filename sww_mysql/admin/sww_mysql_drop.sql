--
-- Created: 12-12-2009.
--
-- Script to drop sww (SubWayWorld) database users and schema.
--
-- Run this script as root.
--
-- mysql -u root -t -vvv -p < scriptname.sql > scriptname_log.txt
--
-- Version Date     Who  Remark
-- 1.0     12-12-09 MVD  Initial version
--

REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'sww_owner'@'localhost';

REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'sww_reader'@'localhost';

DROP SCHEMA sww;

DROP USER 'sww_owner'@'localhost';

DROP USER 'sww_reader'@'localhost';

