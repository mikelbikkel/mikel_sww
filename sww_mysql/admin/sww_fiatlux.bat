REM
REM Drop and recreate subwayworld schemas and database.
REM

mysql -u root -t -vvv -pXXX < sww_mysql_drop.sql > drop.log 2> drop.err

mysql -u root -t -vvv -pXXX < sww_mysql_init.sql > init.log 2> init.err

mysql -u sww_owner -t -vvv -pXXX < sww_mysql_create.sql > create.log 2> create.err

mysql -u sww_owner -t -vvv -pXXX < sww_mysql_views.sql > views.log 2> views.err

mysql -u sww_owner -t -vvv -pXXX < sww_mysql_load.sql > load.log 2> load.err

REM mysql -u sww_owner -t -vvv -pXXX < sww_mysql_alias.sql > alias.log 2> alias.err
