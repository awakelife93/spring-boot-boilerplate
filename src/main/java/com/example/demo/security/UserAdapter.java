package com.example.demo.security;

import com.example.demo.user.constant.UserRole;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserAdapter extends User {

  private SecurityUserItem securityUserItem;

  private static Collection<? extends GrantedAuthority> authorities(
    UserRole role
  ) {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));

    return authorities;
  }

  public UserAdapter(SecurityUserItem securityUserItem) {
    super(
      securityUserItem.getEmail(),
      "",
      authorities(securityUserItem.getRole())
    );
    this.securityUserItem = securityUserItem;
  }
}
