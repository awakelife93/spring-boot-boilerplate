package com.example.demo.common.security;

import com.example.demo.common.constant.UserRole;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.UserAdapter;
import com.example.demo.user.entity.User;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
  implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    final User user = Instancio
      .of(User.class)
      .set(Select.field(User::getId), Long.valueOf(annotation.id()))
      .set(Select.field(User::getEmail), annotation.email())
      .set(Select.field(User::getName), annotation.name())
      .set(Select.field(User::getRole), UserRole.valueOf(annotation.role()))
      .create();

    final UserAdapter userAdapter = new UserAdapter(SecurityUserItem.of(user));

    final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
      userAdapter,
      null,
      userAdapter.getAuthorities()
    );

    securityContext.setAuthentication(usernamePasswordAuthenticationToken);
    return securityContext;
  }
}
