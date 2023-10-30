package com.example.demo.security.exception;

import com.example.demo.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends NotFoundException {

  public TokenNotFoundException(String tokenName) {
    super(HttpStatus.NOT_FOUND, "Is Not Found " + tokenName);
  }
}
