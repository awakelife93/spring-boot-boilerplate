package com.example.demo.common.security;

import com.example.demo.common.BaseIntegrationControllerItem;
import com.example.demo.security.component.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;

public class SecurityItem extends BaseIntegrationControllerItem {

  @Autowired
  protected FilterChainProxy springSecurityFilterChain;

  @MockBean
  protected JWTProvider jwtProvider;

  protected final String unauthorizedMessage =
    "Full authentication is required to access this resource";
}
