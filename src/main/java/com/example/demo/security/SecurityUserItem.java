package com.example.demo.security;

import com.example.demo.common.constant.UserRole;
import com.example.demo.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityUserItem {

  private Long userId;

  private UserRole role;

  private String name;

  private String email;

  @Builder
  public SecurityUserItem(User user) {
    this.userId = user.getId();
    this.role = user.getRole();
    this.name = user.getName();
    this.email = user.getEmail();
  }

  public static SecurityUserItem convertSecurityUserItem(User user) {
    return SecurityUserItem.builder().user(user).build();
  }
}
