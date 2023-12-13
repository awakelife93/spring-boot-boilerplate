package com.example.demo.post.exception;

import com.example.demo.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends NotFoundException {

  public PostNotFoundException() {
    super(HttpStatus.NOT_FOUND, "Is Not Found Post");
  }
}
