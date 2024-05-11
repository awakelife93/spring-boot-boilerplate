package com.example.demo.user.exception;

import com.example.demo.common.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;

public class UserUnAuthorizedException extends UnAuthorizedException {

  public UserUnAuthorizedException(Long userId) {
    super(
      HttpStatus.UNAUTHORIZED,
      String.format("User Not Found userId = %s", userId)
    );
  }

  public UserUnAuthorizedException(String email) {
    super(
      HttpStatus.UNAUTHORIZED,
      String.format("User Not Found email = %s", email)
    );
  }
}
