package com.example.demo.domain.auth.exception;

import com.example.demo.common.exception.AlreadyExistException;
import org.springframework.http.HttpStatus;

public class AlreadyUserExistException extends AlreadyExistException {

  public AlreadyUserExistException() {
    super(HttpStatus.CONFLICT, "Is Already User Exist.");
  }
}
