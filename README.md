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
#### Required software
Jdk 18, Maven 3.8.5, Postgresql 14.4

#### Required database
The application requires empty Postgres 14.4 database.

If one already has available database, connection details may be specified in src/main/resources/application.yml file <br>
If not, one may create docker container with db based on docker-compose file located in /dockerATH

Database schema will be build as specified in src/main/resources/db/migration sql files thanks to flyway

#### Running application

In order to run application open a terminal in the project location and execute command  <br>
`./mvnw spring-boot:run`

Application should be up and running!

Now it's possible to interact with REST API via swagger at http://localhost:8080/swagger-ui/index.html?#/
(login admin, password admin123)
or by using tool like Postman, Insomnia etc

 



