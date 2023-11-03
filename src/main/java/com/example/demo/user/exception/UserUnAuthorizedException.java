package com.example.demo.user.exception;

import com.example.demo.common.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;

public class UserUnAuthorizedException extends UnAuthorizedException {

  public UserUnAuthorizedException() {
    super(HttpStatus.UNAUTHORIZED, "Is Failed Auth.");
  }
}
