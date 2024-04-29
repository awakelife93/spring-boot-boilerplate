package com.example.demo.user.exception;

import com.example.demo.common.exception.AlreadyExistException;
import org.springframework.http.HttpStatus;

public class AlreadyUserExistException extends AlreadyExistException {

  public AlreadyUserExistException() {
    super(HttpStatus.CONFLICT, "Already User Exist");
  }
}
