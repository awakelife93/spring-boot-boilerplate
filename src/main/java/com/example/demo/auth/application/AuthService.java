package com.example.demo.auth.application;

import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.auth.dto.serve.response.RefreshAccessTokenResponse;
import com.example.demo.auth.dto.serve.response.SignInResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.service.TokenService;
import com.example.demo.user.application.UserService;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;
  private final TokenService tokenService;

  public SignInResponse signIn(SignInRequest signInRequest) {
    final User user = userService.validateAuthReturnUser(signInRequest);

    return SignInResponse.of(user, tokenService.createFullTokens(user));
  }

  public void signOut(Long userId) {
    tokenService.deleteRefreshToken(userId);
    SecurityContextHolder.clearContext();
  }

  public RefreshAccessTokenResponse refreshAccessToken(
    SecurityUserItem securityUserItem
  ) {
    return RefreshAccessTokenResponse.of(
      tokenService.refreshAccessToken(securityUserItem)
    );
  }
}
