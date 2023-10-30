### Spring Boot Boilerplate

### Environment & Skills

```
Java / JDK 17
Spring boot 3.1.4
Gradle 8.2.1
Postgresql
Pgadmin
Jpa
QueryDSL
Redis
Spring Security
Lombok
Jwt
Validation
```

### Project Guide

- common
  - Common collection of codes
- controller
  - Folder with domain specific controllers
- service
  - Folder with domain specific services
- domain
  - Collection of properties by application domain
- repository
  - Folder with domain specific repositories
- security
  - Folder with spring security + jwt logic
- util
  - Folder with util logic
- resources
  - prod, dev, local, common, secret
    - secret: A place to write variables that need to be hidden
    - common: A place to write common variables for the project

### Note
- cors
  - This project used **spring security** rather than WebMvcConfigurer for the cors environment.
- docker-compose
  - If you plan to use it, you need to check the environment variables.
- application-secret.yml
  - application-secret.yml is git ignore, please check the example file.

### Author
Hyunwoo Park
