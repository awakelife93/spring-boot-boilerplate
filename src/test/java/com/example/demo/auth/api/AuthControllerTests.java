package com.example.demo.auth.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.auth.application.AuthService;
import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.auth.dto.serve.response.RefreshAccessTokenResponse;
import com.example.demo.auth.dto.serve.response.SignInResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.user.entity.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Auth Controller Test")
@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

  @InjectMocks
  private AuthController authController;

  @Mock
  private AuthService authService;

  private final User user = Instancio.create(User.class);

  private final String defaultAccessToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" + //
    "";

  @Test
  @DisplayName("Sign in")
  public void should_AssertSignInResponse_when_GivenSignInRequest() {
    SignInRequest signInRequest = Instancio.create(SignInRequest.class);

    when(authService.signIn(any(SignInRequest.class)))
      .thenReturn(SignInResponse.of(user, defaultAccessToken));

    ResponseEntity<SignInResponse> response = authController.signIn(
      signInRequest
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    SignInResponse body = response.getBody();
    assertEquals(user.getId(), body.getUserId());
    assertEquals(user.getEmail(), body.getEmail());
    assertEquals(user.getName(), body.getName());
    assertEquals(user.getRole(), body.getRole());
    assertEquals(defaultAccessToken, body.getAccessToken());
  }

  @Test
  @DisplayName("Sign out")
  public void should_AssertSignOutVoidResponse_when_GivenSecurityUserItem() {
    SecurityUserItem securityUserItem = Instancio.create(
      SecurityUserItem.class
    );

    ResponseEntity<Void> response = authController.signOut(securityUserItem);

    assertNotNull(response);
    assertNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName("Refresh access token")
  public void should_AssertRefreshAccessTokenResponse_when_GivenSecurityUserItem() {
    SecurityUserItem securityUserItem = Instancio.create(
      SecurityUserItem.class
    );

    when(authService.refreshAccessToken(any(SecurityUserItem.class)))
      .thenReturn(RefreshAccessTokenResponse.of(defaultAccessToken));

    ResponseEntity<RefreshAccessTokenResponse> response = authController.refreshAccessToken(
      securityUserItem
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    RefreshAccessTokenResponse body = response.getBody();
    assertEquals(defaultAccessToken, body.getAccessToken());
  }
}
