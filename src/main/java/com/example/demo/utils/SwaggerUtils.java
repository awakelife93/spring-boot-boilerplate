package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SwaggerUtils {

  @Value("${springdoc.api-docs.path}")
  private String apiDocsPath;

  public boolean confirmPathEqualsSwaggerConfig(String path) {
    final String swaggerConfigUrl = apiDocsPath + "/swagger-config";
    return path.equals(swaggerConfigUrl);
  }
}
