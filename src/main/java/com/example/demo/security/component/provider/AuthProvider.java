package com.example.demo.security.component.provider;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider {

  @Value("${auth.x-api-key}")
  private String apiKey;

  public String[] ignoreListDefaultEndpoints() {
    String[] endPoints = new String[] {
      "/api-docs/**",
      "/swagger-ui/**",
      "/swagger.html",
      "/h2-console/**",
    };

    return endPoints;
  }

  public String[] whiteListDefaultEndpoints() {
    String[] endPoints = new String[] {
      "/api/v1/auth/signIn",
      "/api/v1/users/register",
    };

    return endPoints;
  }

  public String generateRequestAPIKey(HttpServletRequest request) {
    return request.getHeader("X-API-KEY");
  }

  public boolean validateApiKey(String requestAPIKey) {
    return apiKey.equals(requestAPIKey);
  }
}
