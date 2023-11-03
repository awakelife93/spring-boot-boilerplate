package com.example.demo.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  public static Writer of(Long userId, String email, String name) {
    return Writer.builder().userId(userId).email(email).name(name).build();
  }
}
