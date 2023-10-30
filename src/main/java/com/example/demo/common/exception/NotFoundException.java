package com.example.demo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class NotFoundException extends RuntimeException {

  private HttpStatusCode code = HttpStatus.NOT_FOUND;
  private String message;

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundException(HttpStatusCode code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }
}
