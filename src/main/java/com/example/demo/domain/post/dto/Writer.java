package com.example.demo.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public class Writer {

  private Long userId;

  private String email;

  private String name;

  @Builder
  public Writer(Long userId, String email, String name) {
    this.userId = userId;
    this.email = email;
    this.name = name;
  }
}
