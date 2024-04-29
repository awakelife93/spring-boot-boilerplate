package com.example.demo.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
  info = @Info(
    title = "Spring Boot Boilerplate",
    description = "Spring Boot Boilerplate API Docs",
    version = "v1"
  )
)
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    SecurityScheme securityScheme = new SecurityScheme()
      .type(SecurityScheme.Type.HTTP)
      .scheme("bearer")
      .bearerFormat("JWT")
      .in(SecurityScheme.In.HEADER)
      .name("Authorization");

    SecurityRequirement securityRequirement = new SecurityRequirement()
      .addList("Bearer Token");

    return new OpenAPI()
      .components(
        new Components().addSecuritySchemes("Bearer Token", securityScheme)
      )
      .security(Arrays.asList(securityRequirement));
  }

  @Bean
  public GroupedOpenApi v1OpenApi() {
    return GroupedOpenApi
      .builder()
      .group("V1 API")
      .pathsToMatch("/api/v1/**/**")
      .build();
  }
}
