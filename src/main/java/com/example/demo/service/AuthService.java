package com.example.demo.service;

import com.example.demo.domain.auth.dto.serve.SignInRequest;
import com.example.demo.domain.auth.dto.serve.SignInResponse;
import com.example.demo.domain.auth.exception.AlreadyUserExistException;
import com.example.demo.domain.user.dto.serve.CreateUserRequest;
import com.example.demo.domain.user.dto.serve.CreateUserResponse;
import com.example.demo.domain.user.dto.serve.GetUserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.service.TokenService;
import com.example.demo.service.user.ChangeUserService;
import com.example.demo.service.user.GetUserService;
import com.example.demo.service.user.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;
  private final GetUserService getUserService;
  private final ChangeUserService ChangeUserService;
  private final TokenService tokenService;

  public CreateUserResponse signUp(CreateUserRequest dto) {
    final GetUserResponse confirmUser = getUserService.getUserByEmail(
      dto.getEmail()
    );

    if (!Objects.isNull(confirmUser)) {
      throw new AlreadyUserExistException();
    }

    final User user = ChangeUserService.createUser(dto);

    return CreateUserResponse
      .builder()
      .user(user)
      .accessToken(tokenService.createFullTokens(user))
      .build();
  }

  public SignInResponse signIn(SignInRequest dto) {
    final User user = userService.validateAuthReturnUser(dto);

    return SignInResponse
      .builder()
      .user(user)
      .accessToken(tokenService.createFullTokens(user))
      .build();
  }

  public void signOut(Long userId) {
    tokenService.deleteRefreshToken(userId);
    SecurityContextHolder.clearContext();
  }

  public String refreshAccessToken(SecurityUserItem securityUserItem) {
    return tokenService.refreshAccessToken(securityUserItem);
  }
}
