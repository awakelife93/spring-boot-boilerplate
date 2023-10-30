package com.example.demo.common.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OkResponse {

  private int code;
  private String message;
  private Object data;

  private OkResponse(int code) {
    this.code = code;
  }

  private OkResponse(int code, String message, Object data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  private OkResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public static OkResponse of(final int code) {
    return new OkResponse(code);
  }

  public static OkResponse of(final int code, final String message) {
    return new OkResponse(code, message);
  }

  public static OkResponse of(
    final int code,
    final String message,
    final Object data
  ) {
    return new OkResponse(code, message, data);
  }
}
