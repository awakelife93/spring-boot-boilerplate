package com.example.demo.security.exception;

import com.example.demo.common.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFoundException extends UnAuthorizedException {

  public RefreshTokenNotFoundException() {
    super(HttpStatus.UNAUTHORIZED, String.format("Refresh Token Not Found"));
  }

  public RefreshTokenNotFoundException(Long userId) {
    super(
      HttpStatus.UNAUTHORIZED,
      String.format("Refresh Token Not Found userId = %s", userId)
    );
  }

  public RefreshTokenNotFoundException(String email) {
    super(
      HttpStatus.UNAUTHORIZED,
      String.format("Refresh Token Not Found email = %s", email)
    );
  }
}
