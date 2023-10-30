package com.example.demo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class AlreadyExistException extends RuntimeException {

  private HttpStatusCode code = HttpStatus.CONFLICT;
  private String message;

  public AlreadyExistException(String message) {
    super(message);
  }

  public AlreadyExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlreadyExistException(HttpStatusCode code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }
}
