package com.example.demo.domain.user.dto.serve;

import com.example.demo.common.constant.UserRole;
import com.example.demo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
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
}
