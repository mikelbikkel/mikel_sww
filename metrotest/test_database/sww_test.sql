create schema if not exists sww_test
  default character set utf8 collate utf8_general_ci ;

show warnings;

-- the @'localhost' means that sww_owner can only connect from localhost.
-- use @'%'to allow sww_owner to connect from any host.
create user 'sww_test_owner'@'localhost' identified by 'sww_test_owner';

show warnings;

grant
  alter
, alter routine
, create
, create temporary tables
, create view
, delete
, drop
, execute
, index
, insert
, lock tables
, references
, select
, show view
, trigger
, update
on sww_test.*
to 'sww_test_owner'@'localhost';

show warnings;

create user 'sww_test_reader'@'localhost' identified by 'sww_test_reader';

show warnings;

grant 
  select
  on sww_test.*
  to 'sww_test_reader'@'localhost'
;

show warnings;
