## Simplifying DB Auditing with Spring Boot and Javers
This project is a simple example of how to audit data changes in database by using Spring and Javers

### Requirements
- PostgreSQL database

### How to run
1. `export SPRING_DATASOURCE_URL=...` The format should be `url: jdbc:postgresql://host:port/db_name?sslmode=require&application_name=spring-security-demo&user=...&password=...`. You can easily create PostgreSQL database for free [here](https://rapidapp.io)
2. `./mvnw spring-boot:run`
