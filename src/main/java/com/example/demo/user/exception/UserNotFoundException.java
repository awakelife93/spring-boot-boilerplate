package com.example.demo.user.exception;

import com.example.demo.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Is Not Found User");
  }
}
