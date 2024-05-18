package com.example.demo.auth.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.auth.application.AuthService;
import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.auth.dto.serve.response.RefreshAccessTokenResponse;
import com.example.demo.auth.dto.serve.response.SignInResponse;
import com.example.demo.common.security.SecurityItem;
import com.example.demo.common.security.WithMockCustomUser;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.exception.RefreshTokenNotFoundException;
import com.example.demo.security.service.TokenService;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.exception.UserUnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("test")
@Tag("integration-test")
@DisplayName("integration - Auth Controller Test")
@WebMvcTest(AuthController.class)
@ExtendWith(MockitoExtension.class)
public class AuthIntegrationControllerTests extends SecurityItem {

  @MockBean
  private TokenService tokenService;

  @MockBean
  private AuthService authService;

  private final User user = Instancio.create(User.class);

  private final String defaultUserEmail = "awakelife93@gmail.com";

  private final String defaultUserPassword = "test_password_123!@";

  private final String defaultAccessToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" + //
    "";

  @BeforeEach
  void setUp() {
    mockMvc =
      MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .alwaysDo(print())
        .build();
  }

  @Nested
  @DisplayName("POST /api/v1/auth/signIn Test")
  class SignInTest {

    SignInRequest signInRequest = Instancio
      .of(SignInRequest.class)
      .set(Select.field(SignInRequest::getEmail), defaultUserEmail)
      .set(Select.field(SignInRequest::getPassword), defaultUserPassword)
      .create();

    @Test
    @DisplayName("POST /api/v1/auth/signIn Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToSignInResponse_when_GivenSignInRequest()
      throws Exception {
      when(authService.signIn(any(SignInRequest.class)))
        .thenReturn(SignInResponse.of(user, defaultAccessToken));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/signIn")
            .with(csrf())
            .content(objectMapper.writeValueAsString(signInRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.userId").value(user.getId()))
        .andExpect(jsonPath("$.data.email").value(user.getEmail()))
        .andExpect(jsonPath("$.data.name").value(user.getName()))
        .andExpect(jsonPath("$.data.role").value(user.getRole().name()));
    }

    @Test
    @DisplayName("Field Valid Exception POST /api/v1/auth/signIn Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToValidException_when_GivenWrongSignInRequest()
      throws Exception {
      SignInRequest signInRequest = Instancio
        .of(SignInRequest.class)
        .set(Select.field(SignInRequest::getEmail), "wrong_email_format")
        .set(Select.field(SignInRequest::getPassword), "1234")
        .create();

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/signIn")
            .with(csrf())
            .content(objectMapper.writeValueAsString(signInRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        // email:field email is not email format, password:field password is min size 8 and max size 20,
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("UnAuthorized Exception POST /api/v1/auth/signIn Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToUserUnAuthorizedException_when_GivenSignInRequest()
      throws Exception {
      UserUnAuthorizedException userUnAuthorizedException = new UserUnAuthorizedException(
        user.getId()
      );

      when(authService.signIn(any(SignInRequest.class)))
        .thenThrow(userUnAuthorizedException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/signIn")
            .with(csrf())
            .content(objectMapper.writeValueAsString(signInRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
          jsonPath("$.code").value(userUnAuthorizedException.getCode().value())
        )
        .andExpect(
          jsonPath("$.message").value(userUnAuthorizedException.getMessage())
        )
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("Not Found Exception POST /api/v1/auth/signIn Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToUserNotFoundException_when_GivenSignInRequest()
      throws Exception {
      UserNotFoundException userNotFoundException = new UserNotFoundException(
        user.getId()
      );

      when(authService.signIn(any(SignInRequest.class)))
        .thenThrow(userNotFoundException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/signIn")
            .with(csrf())
            .content(objectMapper.writeValueAsString(signInRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(
          jsonPath("$.code").value(userNotFoundException.getCode().value())
        )
        .andExpect(
          jsonPath("$.message").value(userNotFoundException.getMessage())
        )
        .andExpect(jsonPath("$.errors").isEmpty());
    }
  }

  @Nested
  @DisplayName("POST /api/v1/auth/signOut Test")
  class SignOutTest {

    @Test
    @DisplayName("POST /api/v1/auth/signOut Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToSignOutVoidResponse_when_GivenSecurityUserItemAndUserIsAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/signOut")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage));
    }

    @Test
    @DisplayName("Unauthorized Exception POST /api/v1/auth/signOut Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenSecurityUserItemAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/signOut")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("POST /api/v1/auth/refresh Test")
  class RefreshAccessTokenTest {

    @Test
    @DisplayName("POST /api/v1/auth/refresh Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToRefreshAccessTokenResponse_when_GivenSecurityUserItemAndUserIsAuthenticated()
      throws Exception {
      when(authService.refreshAccessToken(any(SecurityUserItem.class)))
        .thenReturn(RefreshAccessTokenResponse.of(defaultAccessToken));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/refresh")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.accessToken").value(defaultAccessToken));
    }

    @Test
    @DisplayName(
      "Refresh Token Not Found Unauthorized Exception POST /api/v1/auth/refresh Response"
    )
    @WithMockCustomUser
    public void should_ExpectErrorResponseToRefreshTokenNotFoundException_when_GivenSecurityUserItemAndUserIsAuthenticated()
      throws Exception {
      RefreshTokenNotFoundException refreshTokenNotFoundException = new RefreshTokenNotFoundException(
        user.getId()
      );

      when(authService.refreshAccessToken(any(SecurityUserItem.class)))
        .thenThrow(refreshTokenNotFoundException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/refresh")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
          jsonPath("$.code")
            .value(refreshTokenNotFoundException.getCode().value())
        )
        .andExpect(
          jsonPath("$.message")
            .value(refreshTokenNotFoundException.getMessage())
        )
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName(
      "Refresh Token Expired Exception POST /api/v1/auth/refresh Response"
    )
    @WithMockCustomUser
    public void should_ExpectErrorResponseToExpiredJwtException_when_GivenSecurityUserItemAndUserIsAuthenticated()
      throws Exception {
      Claims claims = Instancio.create(Claims.class);

      ExpiredJwtException expiredJwtException = new ExpiredJwtException(
        null,
        claims,
        "JWT expired at ?. Current time: ?, a difference of ? milliseconds.  Allowed clock skew: ? milliseconds."
      );

      when(authService.refreshAccessToken(any(SecurityUserItem.class)))
        .thenThrow(expiredJwtException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/refresh")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(HttpStatus.UNAUTHORIZED.value()))
        .andExpect(
          jsonPath("$.message").value(expiredJwtException.getMessage())
        )
        .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("Unauthorized Exception POST /api/v1/auth/refresh Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenSecurityUserItemAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/auth/refresh")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }
}
