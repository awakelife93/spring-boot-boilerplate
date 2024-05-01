package com.example.demo.user.batch.mapper;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteUserItem {

  private Long id;

  private String name;

  private String email;

  private String role;

  @Builder
  private DeleteUserItem(
    @NonNull Long id,
    @NonNull String email,
    @NonNull String name,
    @NonNull String role
  ) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.role = role;
  }

  public static DeleteUserItem of(
    Long id,
    String email,
    String name,
    String role
  ) {
    return DeleteUserItem
      .builder()
      .id(id)
      .email(email)
      .name(name)
      .role(role)
      .build();
  }
}
