package com.example.demo.auth.dto.serve.response;

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
public class SignInResponse {

  private Long userId;

  private UserRole role;

  private String name;

  private String email;

  private String accessToken;

  @Builder
  public SignInResponse(User user, String accessToken) {
    this.userId = user.getId();
    this.role = user.getRole();
    this.name = user.getName();
    this.email = user.getEmail();
    this.accessToken = accessToken;
  }

  public static SignInResponse of(User user, String accessToken) {
    return SignInResponse.builder().user(user).accessToken(accessToken).build();
  }
}
