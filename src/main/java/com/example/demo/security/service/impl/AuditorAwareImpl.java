package com.example.demo.security.service.impl;

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

    Object principal = authentication.getPrincipal();

    if (principal.equals("anonymousUser")) {
      return null;
    }

    UserAdapter userAdapter = (UserAdapter) principal;

    SecurityUserItem securityUserItem = userAdapter.getSecurityUserItem();

    return Optional.of(securityUserItem.getUserId());
  }
}
