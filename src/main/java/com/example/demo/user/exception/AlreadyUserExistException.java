package com.example.demo.user.exception;

import com.example.demo.common.exception.AlreadyExistException;
import org.springframework.http.HttpStatus;

public class AlreadyUserExistException extends AlreadyExistException {

  public AlreadyUserExistException(Long userId) {
    super(
      HttpStatus.CONFLICT,
      String.format("User Not Found userId = %s", userId)
    );
  }

  public AlreadyUserExistException(String email) {
    super(
      HttpStatus.CONFLICT,
      String.format("User Not Found email = %s", email)
    );
  }
}
