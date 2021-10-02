#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    create table
    	applications ( code varchar(255) not null,
    	name varchar(255),
    	primary key (code) );

    create table
    	authorities ( id bigserial not null,
    	enabled_group_id int8 not null,
    	role_id int8 not null,
    	primary key (id) );

    create table
    	enabled_groups ( id bigserial not null,
    	ldap_group_name varchar(255),
    	application_code varchar(255) not null,
    	primary key (id) );

    create table
    	facilities ( code varchar(255) not null,
    	name varchar(255),
    	primary key (code) );

    create table
    	grants ( id bigserial not null,
    	description varchar(255),
    	name varchar(255),
    	application_code varchar(255) not null,
    	primary key (id) );

    create table
    	roles ( id bigserial not null,
    	name varchar(255),
    	priority int4 not null,
    	application_code varchar(255) not null,
    	facility_code varchar(255) not null,
    	primary key (id) );

    create table
    	roles_grants ( role_id int8 not null,
    	grant_id int8 not null,
    	primary key (role_id,
    	grant_id) );

    alter table
    	authorities add constraint auth_enabled_group_fk foreign key (enabled_group_id) references enabled_groups;
    alter table
    	authorities add constraint auth_role_fk foreign key (role_id) references roles;
    create index
      authorities_enabled_group_id_idx ON authorities (enabled_group_id);
    create index
      authorities_role_id_idx ON authorities (role_id);

    alter table
    	enabled_groups add constraint eg_application_fk foreign key (application_code) references applications;
    create index
      enabled_groups_application_code_idx ON enabled_groups (application_code);

    alter table
    	grants add constraint grants_application_fk foreign key (application_code) references applications;
    create index
      grants_application_code_idx ON grants (application_code);

    alter table
    	roles add constraint roles_application_fk foreign key (application_code) references applications;
    alter table
    	roles add constraint roles_facility_fk foreign key (facility_code) references facilities;
    create index
      roles_application_code_idx ON roles (application_code);
    create index
      roles_facility_code_idx ON roles (facility_code);

    alter table
    	roles_grants add constraint rg_grant_fk foreign key (grant_id) references grants;
    alter table
    	roles_grants add constraint rg_role_fk foreign key (role_id) references roles;
EOSQL