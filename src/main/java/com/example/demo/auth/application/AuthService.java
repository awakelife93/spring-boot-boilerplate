package com.example.demo.auth.application;

import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.auth.dto.serve.response.RefreshAccessTokenResponse;
import com.example.demo.auth.dto.serve.response.SignInResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.component.provider.TokenProvider;
import com.example.demo.user.application.UserService;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;
  private final TokenProvider tokenProvider;

  public SignInResponse signIn(SignInRequest signInRequest) {
    final User user = userService.validateAuthReturnUser(signInRequest);

    return SignInResponse.of(user, tokenProvider.createFullTokens(user));
  }

  public void signOut(Long userId) {
    tokenProvider.deleteRefreshToken(userId);
    SecurityContextHolder.clearContext();
  }

  public RefreshAccessTokenResponse refreshAccessToken(
    SecurityUserItem securityUserItem
  ) {
    return RefreshAccessTokenResponse.of(
      tokenProvider.refreshAccessToken(securityUserItem)
    );
  }
}
