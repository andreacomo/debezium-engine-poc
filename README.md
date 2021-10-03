# Debezium Engine POC

POC project for studying CDC with a Spring Boot application listening on PostgreSQL data changes (via `pgoutput` plugin).

Docker compose starts a PostgreSQL server with proper configuration for enabling CDC (aka **publications**)

Some references:

* [Debezium Engine](https://debezium.io/documentation/reference/1.6/development/engine.html)
* [PostgreSQL Connector](https://debezium.io/documentation/reference/1.6/connectors/postgresql.html)