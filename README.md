### Spring Boot Boilerplate

### Environment & Skills

- Application
  - Java / JDK 17
  - Spring boot 3.3.0
  - Gradle 8.2.1
  - Spring Security 6.1.4
  - Spring Batch 5.0.3
  - Springdoc OpenAPI
  - Postgresql
  - Jpa
  - QueryDSL
  - Redis
  - Lombok
  - Jwt
  - Validation

- Test
  - Spring Boot Starter Test
  - Spring Security
  - Spring Batch
  - Junit 5
  - Mockito
  - Instancio
  - h2 database (PostgreSQL mode)

- Tools
  - Pgadmin

### Project Guide

- common
- domain (post, user, auth)
- security
  - spring security + jwt logic
- utils
- resources
  - prod, dev, local, common, secret, test
    - secret: Write variables that need to be hidden.
    - common: Write common variables for the project.
    - test: Create the variables needed for your test environment.

### Note
- cors
  - This project used **spring security** rather than WebMvcConfigurer for the cors environment.
- docker-compose
  - If you plan to use it, you need to check the environment variables.
- application-secret.yml
  - application-secret.yml is git ignore, please check the example file.
- create spring batch metadata table (localhost, development and production environments.)
  - Run your ddl script or Please refer to [github - spring batch ](https://github.com/spring-projects/spring-batch/blob/5.0.x/spring-batch-core/src/main/resources/org/springframework/batch/core/schema-postgresql.sql)
    - Since this project uses postgresql, the spring.batch.jdbc.initialize-schema: always option does not work.
    - test environment, generating [batch-postgresql-metadata-schema.sql](src/main/resources/sql/batch-postgresql-metadata-schema.sql).
      - [application-test.yml](src/main/resources/application-test.yml)

### Author
Hyunwoo Park
