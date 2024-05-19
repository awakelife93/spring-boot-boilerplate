package com.example.demo.common.security;

import com.example.demo.common.BaseIntegrationControllerItem;
import com.example.demo.security.component.provider.JWTProvider;
import org.springframework.boot.test.mock.mockito.MockBean;

public class SecurityItem extends BaseIntegrationControllerItem {

  @MockBean
  protected JWTProvider jwtProvider;

  protected final String unauthorizedMessage =
    "Full authentication is required to access this resource";
}
