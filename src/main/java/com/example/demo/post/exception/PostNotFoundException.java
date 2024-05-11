package com.example.demo.post.exception;

import com.example.demo.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends NotFoundException {

  public PostNotFoundException(Long postId) {
    super(
      HttpStatus.NOT_FOUND,
      String.format("Post Not Found postId = %s", postId)
    );
  }
}
