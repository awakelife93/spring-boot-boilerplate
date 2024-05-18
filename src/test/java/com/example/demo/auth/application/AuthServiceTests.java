package com.example.demo.auth.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.auth.dto.serve.response.RefreshAccessTokenResponse;
import com.example.demo.auth.dto.serve.response.SignInResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.exception.RefreshTokenNotFoundException;
import com.example.demo.security.service.TokenService;
import com.example.demo.user.application.impl.UserServiceImpl;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.exception.UserUnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Auth Service Test")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

  @Mock
  private TokenService tokenService;

  @Mock
  private UserServiceImpl userServiceImpl;

  @InjectMocks
  private AuthService authService;

  private final User user = Instancio.create(User.class);

  private final String defaultAccessToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" + //
    "";

  @Nested
  @DisplayName("Sign In Test")
  class SignInTest {

    SignInRequest signInRequest = Instancio.create(SignInRequest.class);

    @Test
    @DisplayName("Success sign in")
    public void should_AssertSignInResponse_when_GivenSignInRequest() {
      when(userServiceImpl.validateAuthReturnUser(any(SignInRequest.class)))
        .thenReturn(user);
      when(tokenService.createFullTokens(any(User.class)))
        .thenReturn(defaultAccessToken);

      SignInResponse signInResponse = authService.signIn(signInRequest);

      assertNotNull(signInResponse);
      assertEquals(user.getEmail(), signInResponse.getEmail());
      assertEquals(user.getName(), signInResponse.getName());
      assertEquals(defaultAccessToken, signInResponse.getAccessToken());
    }

    @Test
    @DisplayName("User not found")
    public void should_AssertUserNotFoundException_when_GivenSignInRequest() {
      when(userServiceImpl.validateAuthReturnUser(any(SignInRequest.class)))
        .thenThrow(new UserNotFoundException(user.getId()));

      assertThrows(
        UserNotFoundException.class,
        () -> authService.signIn(signInRequest)
      );
    }

    @Test
    @DisplayName("User unauthorized")
    public void should_AssertUserUnAuthorizedException_when_GivenSignInRequest() {
      when(userServiceImpl.validateAuthReturnUser(any(SignInRequest.class)))
        .thenThrow(new UserUnAuthorizedException(user.getEmail()));

      assertThrows(
        UserUnAuthorizedException.class,
        () -> authService.signIn(signInRequest)
      );
    }
  }

  @Nested
  @DisplayName("Sign Out Test")
  class SignOutTest {

    @Test
    @DisplayName("Success sign out")
    public void should_VerifyCallDeleteRefreshToken_when_GivenUserId() {
      authService.signOut(user.getId());

      verify(tokenService, times(1)).deleteRefreshToken(anyLong());
    }
  }

  @Nested
  @DisplayName("Refresh Access Token Test")
  class RefreshTokenTest {

    SecurityUserItem securityUserItem = Instancio.create(
      SecurityUserItem.class
    );

    @Test
    @DisplayName("Success refresh access token")
    public void should_AssertRefreshAccessTokenResponse_when_GivenSecurityUserItem() {
      when(tokenService.refreshAccessToken(any(SecurityUserItem.class)))
        .thenReturn(defaultAccessToken);

      RefreshAccessTokenResponse refreshAccessTokenResponse = authService.refreshAccessToken(
        securityUserItem
      );

      assertNotNull(refreshAccessTokenResponse);
      assertEquals(
        defaultAccessToken,
        refreshAccessTokenResponse.getAccessToken()
      );
    }

    @Test
    @DisplayName("Refresh token is expired")
    public void should_AssertExpiredJwtException_when_GivenSecurityUserItem() {
      Claims claims = Instancio.create(Claims.class);

      when(tokenService.refreshAccessToken(any(SecurityUserItem.class)))
        .thenThrow(
          new ExpiredJwtException(
            null,
            claims,
            "JWT expired at ?. Current time: ?, a difference of ? milliseconds.  Allowed clock skew: ? milliseconds."
          )
        );

      assertThrows(
        ExpiredJwtException.class,
        () -> authService.refreshAccessToken(securityUserItem)
      );
    }

    @Test
    @DisplayName("Refresh token is not found")
    public void should_AssertRefreshTokenNotFoundException_when_GivenSecurityUserItem() {
      when(tokenService.refreshAccessToken(any(SecurityUserItem.class)))
        .thenThrow(new RefreshTokenNotFoundException(user.getId()));

      assertThrows(
        RefreshTokenNotFoundException.class,
        () -> authService.refreshAccessToken(securityUserItem)
      );
    }
  }
}
