package com.example.demo.common.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

public class BadRequestException extends HttpClientErrorException {

  public BadRequestException(HttpStatusCode code) {
    super(code);
  }

  public BadRequestException(HttpStatusCode code, String message) {
    super(code, message);
  }
}
