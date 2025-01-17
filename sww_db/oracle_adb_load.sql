BEGIN
DBMS_CLOUD.COPY_DATA
( table_name => 'CONTINENT'
, credential_name => 'OCI$RESOURCE_PRINCIPAL'
, file_uri_list => 'https://axncfsv3ayxy.objectstorage.eu-amsterdam-1.oci.customer-oci.com/n/axncfsv3ayxy/b/load_ora/o/sww%2Fcontinent_20250112.csv'
, format => json_object('delimiter' value ';','characterset' value 'UTF8','skipheaders' value '1','type' value 'csv')
);
END;

BEGIN
DBMS_CLOUD.COPY_DATA
( table_name => 'COUNTRY'
, credential_name => 'OCI$RESOURCE_PRINCIPAL'
, file_uri_list => 'https://axncfsv3ayxy.objectstorage.eu-amsterdam-1.oci.customer-oci.com/n/axncfsv3ayxy/b/load_ora/o/sww%2Fcountry_20250112.csv'
, format => json_object('delimiter' value ';','characterset' value 'UTF8','skipheaders' value '1','type' value 'csv')
);
END;

BEGIN
DBMS_CLOUD.COPY_DATA
( table_name => 'CITY'
, credential_name => 'OCI$RESOURCE_PRINCIPAL'
, file_uri_list => 'https://axncfsv3ayxy.objectstorage.eu-amsterdam-1.oci.customer-oci.com/n/axncfsv3ayxy/b/load_ora/o/sww%2Fcity_20250112.csv'
, format => json_object('delimiter' value ';','characterset' value 'UTF8','skipheaders' value '1','type' value 'csv')
);
END;


-- show result of copy_data
select * from user_load_operations;

-- remove log and bad tables for the operations.
begin
  dbms_cloud.delete_all_operations;
end;
