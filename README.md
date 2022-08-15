# Airsoft Tech Helper
Backend of online Spring Boot application for managing airsoft tech service. 
I'm building this application to practice and learn Spring Boot.

Main aim is to simply keep track of progress in builds for clients and parts and their versions.

## Technologies
- Java 18
- Spring Boot 2.7.0
- Spring Security
- Spring Data Jpa
- Spring Web
- Hibernate ORM
- Hibernate Validator
- Spring Doc OpenAPI
- PostgreSQL 14
- JUnit5 tests
- H2 1.4.20 in-memory db for tests
- Flyway 8.5.13
- Lombok
- Maven

## Launch
Requirements: Jdk 18, Maven 3.8.5, Postgresql 14.4

The application requires empty PostgreSQL database. One may create docker container based on docker-compose file located in /dockerATH

Alternatively one may specify connection to postgres database in 
src/main/resources/application.yml file

Database schema will be build as specified in src/main/resources/db/migration sql files thanks to flyway

After database is ready open in terminal project location and execute command

`./mvnw spring-boot:run`

Application should be up and running!

Now it's possible to interact with REST API via swagger at http://localhost:8080/swagger-ui/index.html?#/
(login admin, password admin123)
or by using tool like Postman, Insomnia etc

 



