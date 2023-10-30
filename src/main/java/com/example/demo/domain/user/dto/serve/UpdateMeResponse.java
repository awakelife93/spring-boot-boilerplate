package com.example.demo.domain.user.dto.serve;

import com.example.demo.common.constant.UserRole;
import com.example.demo.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMeResponse {

  private Long userId;

  private UserRole role;

  private String name;

  private String email;

  private String accessToken;

  @Builder
  public UpdateMeResponse(User user, String accessToken) {
    this.userId = user.getId();
    this.role = user.getRole();
    this.name = user.getName();
    this.email = user.getEmail();
    this.accessToken = accessToken;
  }

  public static UpdateMeResponse of(User user, String accessToken) {
    return UpdateMeResponse
      .builder()
      .user(user)
      .accessToken(accessToken)
      .build();
  }
}
