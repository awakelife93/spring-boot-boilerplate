### Spring Boot Boilerplate

### Environment & Skills

```
Java / JDK 17
Spring boot 3.1.4
Gradle 8.2.1
Spring Security 6.1.4
Spring Batch 5.0.3
Springdoc OpenAPI
Postgresql
Pgadmin
Jpa
QueryDSL
Redis
Lombok
Jwt
Validation
```

### Project Guide

- common
  - Common collection of codes
- domain (post, user, auth)
  - Collection of properties by application domain
- security
  - Folder with spring security + jwt logic
- util
  - Folder with util logic
- resources
  - prod, dev, local, common, secret, batch
    - secret: This file to write variables that need to be hidden
    - common: This file to write common variables for the project
    - batch: This file to write batch variables for the project

### Note
- cors
  - This project used **spring security** rather than WebMvcConfigurer for the cors environment.
- docker-compose
  - If you plan to use it, you need to check the environment variables.
- application-secret.yml
  - application-secret.yml is git ignore, please check the example file.
- create spring batch metadata table
  - Run your ddl script or Please refer to [github - spring batch ](https://github.com/spring-projects/spring-batch/blob/5.0.x/spring-batch-core/src/main/resources/org/springframework/batch/core/schema-postgresql.sql)
    - Since this project uses postgresql, the spring.batch.jdbc.initialize-schema: always option does not work.

### Author
Hyunwoo Park
