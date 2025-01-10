create json RELATIONAL DUALITY VIEW sww_continent_dv as
select JSON { '_id': c.continent_id,
              'description': c.description,
              'cities' :
              [ select json { 'city_id' : ct.city_id,
                              'city_code' : ct.city_code}
                              from city ct
                              where ct.continent_id = c.continent_id ]}
from continent c;

select data from sww_continent_dv;

-- https://kuassimensah.medium.com/whats-in-oracle-db-23c-free-developer-release-for-java-developers-26931081682c

------------------------------------------------------------------
-- PostgreSQL
------------------------------------------------------------------
select json_agg(c) from continent c;
select row_to_json(c) from continent c; 

select c.continent_id, c.description, ct.city_id
from continent c
     join city ct on ct.continent_id = c.continent_id
;

select to_jsonb(tmp) - 'continent_id'
from
(
select c.continent_id, c.description, ct.city_id
from continent c
     join city ct on ct.continent_id = c.continent_id
) tmp;

select json_agg(to_jsonb(tmp) - 'continent_id')
from
(
select c.continent_id, c.description, ct.city_id
from continent c
     join city ct on ct.continent_id = c.continent_id
) tmp;

select con.continent_id, json_agg(cities order by city_id desc)
from continent con
     join
     (select ct.continent_id, ct.city_id, ct.description city_name
      from   city ct) cities
	 on con.continent_id = cities.continent_id
group by con.continent_id
;

select con.continent_id, json_agg(cities order by city_id desc)
from continent con
     join
     (select ct.continent_id, ct.city_id, ct.description city_name
      from   city ct) cities
	 on con.continent_id = cities.continent_id
group by con.continent_id
;

select json_agg(tmp)
from
(
select con.continent_id, json_agg(cities)
from continent con
     join
     (select ct.continent_id, ct.city_id, ct.description city_name
      from   city ct) cities
	 on con.continent_id = cities.continent_id
group by con.continent_id
) tmp;


select json_agg(tmp order by continent_id desc)
from (
select con.continent_id, json_agg(json_build_object('c_id', city_id, 'c_name', city_name)) cts
from continent con
     join
     (select ct.continent_id, ct.city_id, ct.description city_name
      from   city ct) cities
	 on con.continent_id = cities.continent_id
where con.continent_id in ('OCEANIA', 'AFRICA') 
group by con.continent_id
) tmp;


https://docs.singlestore.com/cloud/reference/sql-reference/json-functions/json-agg/



WITH
-- strip down employees table
employees AS (
  SELECT department_id, name, start_date
  FROM employees
),
-- join to departments table and aggregate
departments AS (
  SELECT d.name AS department_name,
         json_agg(e) AS employees
 FROM departments d
  JOIN employees e
  USING (department_id)
  GROUP BY d.name
)
-- output as one json list
SELECT json_agg(departments)
FROM departments;


https://dev.to/johndotowl/postgresql-17-installation-on-ubuntu-2404-5bfi
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo gpg --dearmor -o /etc/apt/trusted.gpg.d/postgresql.gpg
sudo apt update
sudo apt install postgresql-17

Selecting previously unselected package libjson-perl.
(Reading database ... 51556 files and directories currently installed.)
Preparing to unpack .../00-libjson-perl_4.04000-1_all.deb ...
Unpacking libjson-perl (4.04000-1) ...
Selecting previously unselected package postgresql-client-common.
Preparing to unpack .../01-postgresql-client-common_264.pgdg22.04+1_all.deb ...
Unpacking postgresql-client-common (264.pgdg22.04+1) ...
Selecting previously unselected package ssl-cert.
Preparing to unpack .../02-ssl-cert_1.1.2_all.deb ...
Unpacking ssl-cert (1.1.2) ...
Selecting previously unselected package postgresql-common.
Preparing to unpack .../03-postgresql-common_264.pgdg22.04+1_all.deb ...
Adding 'diversion of /usr/bin/pg_config to /usr/bin/pg_config.libpq-dev by postgresql-common'
Unpacking postgresql-common (264.pgdg22.04+1) ...
Selecting previously unselected package libcommon-sense-perl:amd64.
Preparing to unpack .../04-libcommon-sense-perl_3.75-2build1_amd64.deb ...
Unpacking libcommon-sense-perl:amd64 (3.75-2build1) ...
Selecting previously unselected package libtypes-serialiser-perl.
Preparing to unpack .../05-libtypes-serialiser-perl_1.01-1_all.deb ...
Unpacking libtypes-serialiser-perl (1.01-1) ...
Selecting previously unselected package libjson-xs-perl.
Preparing to unpack .../06-libjson-xs-perl_4.030-1build3_amd64.deb ...
Unpacking libjson-xs-perl (4.030-1build3) ...
Selecting previously unselected package libpq5:amd64.
Preparing to unpack .../07-libpq5_17.0-1.pgdg22.04+1_amd64.deb ...
Unpacking libpq5:amd64 (17.0-1.pgdg22.04+1) ...
Selecting previously unselected package libxslt1.1:amd64.
Preparing to unpack .../08-libxslt1.1_1.1.34-4ubuntu0.22.04.1_amd64.deb ...
Unpacking libxslt1.1:amd64 (1.1.34-4ubuntu0.22.04.1) ...
Selecting previously unselected package postgresql-client-17.
Preparing to unpack .../09-postgresql-client-17_17.0-1.pgdg22.04+1_amd64.deb ...
Unpacking postgresql-client-17 (17.0-1.pgdg22.04+1) ...
Selecting previously unselected package postgresql-17.
Preparing to unpack .../10-postgresql-17_17.0-1.pgdg22.04+1_amd64.deb ...
Unpacking postgresql-17 (17.0-1.pgdg22.04+1) ...
Selecting previously unselected package sysstat.
Preparing to unpack .../11-sysstat_12.5.2-2ubuntu0.2_amd64.deb ...
Unpacking sysstat (12.5.2-2ubuntu0.2) ...
Setting up postgresql-client-common (264.pgdg22.04+1) ...
Installing new version of config file /etc/postgresql-common/supported_versions ...
Setting up libpq5:amd64 (17.0-1.pgdg22.04+1) ...
Setting up libcommon-sense-perl:amd64 (3.75-2build1) ...
Setting up ssl-cert (1.1.2) ...
Setting up libtypes-serialiser-perl (1.01-1) ...
Setting up libjson-perl (4.04000-1) ...
Setting up libxslt1.1:amd64 (1.1.34-4ubuntu0.22.04.1) ...
Setting up sysstat (12.5.2-2ubuntu0.2) ...
update-alternatives: using /usr/bin/sar.sysstat to provide /usr/bin/sar (sar) in auto mode
Setting up libjson-xs-perl (4.030-1build3) ...
Setting up postgresql-client-17 (17.0-1.pgdg22.04+1) ...
update-alternatives: using /usr/share/postgresql/17/man/man1/psql.1.gz to provide /usr/share/man/man1/psql.1.gz (psql.1.gz) in auto mode
Setting up postgresql-common (264.pgdg22.04+1) ...
Replacing config file /etc/postgresql-common/createcluster.conf with new version
'/etc/apt/trusted.gpg.d/apt.postgresql.org.gpg' -> '/usr/share/postgresql-common/pgdg/apt.postgresql.org.gpg'
Setting up postgresql-17 (17.0-1.pgdg22.04+1) ...
Creating new PostgreSQL cluster 17/main ...
/usr/lib/postgresql/17/bin/initdb -D /var/lib/postgresql/17/main --auth-local peer --auth-host scram-sha-256 --no-instructions
The files belonging to this database system will be owned by user "postgres".
This user must also own the server process.

The database cluster will be initialized with locale "C.UTF-8".
The default database encoding has accordingly been set to "UTF8".
The default text search configuration will be set to "english".

Data page checksums are disabled.

fixing permissions on existing directory /var/lib/postgresql/17/main ... ok
creating subdirectories ... ok
selecting dynamic shared memory implementation ... posix
selecting default "max_connections" ... 100
selecting default "shared_buffers" ... 128MB
selecting default time zone ... Europe/Amsterdam
creating configuration files ... ok
running bootstrap script ... ok
performing post-bootstrap initialization ... ok
syncing data to disk ... ok
Processing triggers for man-db (2.10.2-1) ...
Processing triggers for libc-bin (2.35-0ubuntu3.8) ...

sudo systemctl start postgresql
sudo systemctl enable postgresql

sudo nano /etc/postgresql/17/main/postgresql.conf
listen_addresses = '*'

sudo sed -i '/^host/s/ident/md5/' /etc/postgresql/17/main/pg_hba.conf
sudo sed -i '/^local/s/peer/trust/' /etc/postgresql/17/main/pg_hba.conf
echo "host all all 0.0.0.0/0 md5" | sudo tee -a /etc/postgresql/17/main/pg_hba.conf

sudo systemctl restart postgresql

sudo ufw allow 5432/tcp
sudo -u postgres psql
ALTER USER postgres PASSWORD 'MicheL66';

sudo -i -u postgres

