package com.example.demo.user.batch.mapper;

import java.time.LocalDateTime;
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

  private LocalDateTime deletedDt;

  @Builder
  private DeleteUserItem(
    @NonNull Long id,
    @NonNull String email,
    @NonNull String name,
    @NonNull String role,
    @NonNull LocalDateTime deletedDt
  ) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.role = role;
    this.deletedDt = deletedDt;
  }

  public static DeleteUserItem of(
    Long id,
    String email,
    String name,
    String role,
    LocalDateTime deletedDt
  ) {
    return DeleteUserItem
      .builder()
      .id(id)
      .email(email)
      .name(name)
      .role(role)
      .deletedDt(deletedDt)
      .build();
  }
}
