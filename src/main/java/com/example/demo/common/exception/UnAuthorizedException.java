package com.example.demo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class UnAuthorizedException extends RuntimeException {

  private HttpStatusCode code = HttpStatus.UNAUTHORIZED;
  private String message;

  public UnAuthorizedException(String message) {
    super(message);
  }

  public UnAuthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnAuthorizedException(HttpStatusCode code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }
}
