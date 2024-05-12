package com.example.demo.security;

import com.example.demo.user.constant.UserRole;
import com.example.demo.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

  public static SecurityUserItem of(User user) {
    return SecurityUserItem.builder().user(user).build();
  }
}
