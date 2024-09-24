-- Create owner for the SWW test-schema
CREATE USER testwrap WITH
LOGIN
NOSUPERUSER
NOCREATEDB
NOCREATEROLE
NOINHERIT
NOREPLICATION
CONNECTION LIMIT -1;

ALTER ROLE testwrap IN DATABASE postgres SET default_tablespace TO 'ts_app';
ALTER ROLE testwrap IN DATABASE postgres SET client_encoding TO 'UTF-8';

ALTER USER testwrap PASSWORD 'testwrap';

-- Create schema.
CREATE SCHEMA testwrap AUTHORIZATION testwrap;
COMMENT ON SCHEMA testwrap IS 'Database to test the dbwrapper';

-- Grants for testwrap
GRANT CREATE ON TABLESPACE ts_app TO testwrap;

CREATE USER testwrap_writer WITH
LOGIN
NOSUPERUSER
NOCREATEDB
NOCREATEROLE
NOINHERIT
NOREPLICATION
CONNECTION LIMIT -1
PASSWORD 'testwrap_writer';

GRANT USAGE ON SCHEMA testwrap to testwrap_writer;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA testwrap TO testwrap_writer;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA testwrap TO testwrap_writer;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA testwrap TO testwrap_writer;


-- JDBC connect string:
-- jdbc:postgresql://localhost:5433/postgres?currentSchema=testwrap
-- Database: postgres
-- User: testwrap
-- Properties: currentSchema=testwrap
-- Driver: org.postgresql.Driver
-- Maven dependencies:
--    <dependency>
--      <groupId>org.postgresql</groupId>
--      <artifactId>postgresql</artifactId>
--      <version>9.4.1212.jre7</version>
--      <scope>provided</scope>
--    </dependency>
