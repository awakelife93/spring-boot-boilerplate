package com.example.demo.security.service.impl;

import com.example.demo.domain.user.entity.User;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.UserAdapter;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String userId)
    throws UsernameNotFoundException {
    User user = userService.validateReturnUser(Long.valueOf(userId));

    return new UserAdapter(SecurityUserItem.of(user));
  }
}
