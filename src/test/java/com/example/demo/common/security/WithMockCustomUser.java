package com.example.demo.common.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
  String id() default "1";

  String email() default "awakelife93@gmail.com";

  String name() default "awakelife93";

  String role() default "USER";
}
