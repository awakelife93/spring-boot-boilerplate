package com.example.demo.common.service.impl;

import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.UserAdapter;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<Long> {

  @Override
  public Optional<Long> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();

    if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
      return null;
    }

    UserAdapter userAdapter = (UserAdapter) authentication.getPrincipal();
    SecurityUserItem securityUserItem = userAdapter.getSecurityUserItem();

    return Optional.of(securityUserItem.getUserId());
  }
}
