#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE ROLE cdc REPLICATION LOGIN PASSWORD 'cdc_pwd';
    GRANT SELECT ON ALL TABLES IN SCHEMA public TO cdc;

    CREATE PUBLICATION "cdc-publication" FOR ALL TABLES

    --Config required only to let Debezium create a publication
    --GRANT CREATE ON DATABASE "$POSTGRES_DB" TO cdc;
    --CREATE ROLE cdc_group;
    --GRANT cdc_group TO "$POSTGRES_USER";
    --GRANT cdc_group TO cdc;

    --ALTER TABLE applications OWNER TO cdc_group;
    --ALTER TABLE facilities OWNER TO cdc_group;
    --ALTER TABLE enabled_groups OWNER TO cdc_group;
    --ALTER TABLE roles OWNER TO cdc_group;
    --ALTER TABLE grants OWNER TO cdc_group;
    --ALTER TABLE roles_grants OWNER TO cdc_group;
    --ALTER TABLE authorities OWNER TO cdc_group;
EOSQL