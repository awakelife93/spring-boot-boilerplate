package com.example.demo.security.exception;

import com.example.demo.common.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;

public class APIKeyNotFoundException extends UnAuthorizedException {

  public APIKeyNotFoundException() {
    super(HttpStatus.UNAUTHORIZED, String.format("API Key Not Found"));
  }

  public APIKeyNotFoundException(String requestURI) {
    super(
      HttpStatus.UNAUTHORIZED,
      String.format("API Key Not Found requestURI = %s", requestURI)
    );
  }
}
