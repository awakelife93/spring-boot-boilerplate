package com.example.demo.user.exception;

import com.example.demo.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(Long userId) {
    super(
      HttpStatus.NOT_FOUND,
      String.format("User Not Found userId = %s", userId)
    );
  }

  public UserNotFoundException(String email) {
    super(
      HttpStatus.NOT_FOUND,
      String.format("User Not Found email = %s", email)
    );
  }
}
