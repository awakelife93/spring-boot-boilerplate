package com.example.demo.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ErrorResponse {

  @Schema(description = "Error Code", nullable = false)
  private int code;

  @Schema(description = "Error Message", nullable = false)
  private String message;

  @Schema(description = "Error Item", nullable = false)
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
