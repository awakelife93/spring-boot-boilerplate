package com.example.demo.user.dto.serve;

import com.example.demo.common.constant.UserRole;
import com.example.demo.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserResponse {

  private Long userId;

  private UserRole role;

  private String name;

  private String email;

  @Builder
  public UpdateUserResponse(User user) {
    this.userId = user.getId();
    this.role = user.getRole();
    this.name = user.getName();
    this.email = user.getEmail();
  }

  public static UpdateUserResponse of(User user) {
    return UpdateUserResponse.builder().user(user).build();
  }
}
