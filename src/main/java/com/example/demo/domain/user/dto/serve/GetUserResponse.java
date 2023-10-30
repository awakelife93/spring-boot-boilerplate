package com.example.demo.domain.user.dto.serve;

import com.example.demo.common.constant.UserRole;
import com.example.demo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public class GetUserResponse {

  private Long userId;

  private UserRole role;

  private String name;

  private String email;

  @Builder
  public GetUserResponse(User user) {
    this.userId = user.getId();
    this.role = user.getRole();
    this.name = user.getName();
    this.email = user.getEmail();
  }
}
