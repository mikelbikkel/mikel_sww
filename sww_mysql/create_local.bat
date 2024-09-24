setlocal

set dbhost=localhost
set dbport=3306
set db=sww

call clean.bat

mysql -u sww_owner -t -vvv --host=%dbhost% --port=%dbport% --database=%db% -psww_owner < sww_mysql_create.sql > create.log 2> create.err

mysql -u sww_owner -t -vvv --host=%dbhost% --port=%dbport% --database=%db% -psww_owner < sww_mysql_views.sql > views.log 2> views.err

mysql -u sww_owner -t -vvv --host=%dbhost% --port=%dbport% --database=%db% -psww_owner < sww_mysql_load.sql > load.log 2> load.err

endlocal
