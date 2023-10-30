package com.example.demo.common.response;

import lombok.Getter;

@Getter
public class ErrorResponse {

  private int code;
  private String message;
  private Object errors;

  private ErrorResponse(int code) {
    this.code = code;
  }

  private ErrorResponse(int code, String message, Object errors) {
    this.code = code;
    this.message = message;
    this.errors = errors;
  }

  private ErrorResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ErrorResponse of(final int code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(final int code, final String message) {
    return new ErrorResponse(code, message);
  }

  public static ErrorResponse of(
    final int code,
    final String message,
    final Object errors
  ) {
    return new ErrorResponse(code, message, errors);
  }
}
