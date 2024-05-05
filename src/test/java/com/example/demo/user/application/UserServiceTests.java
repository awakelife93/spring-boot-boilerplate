package com.example.demo.user.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.user.application.impl.UserServiceImpl;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.exception.UserUnAuthorizedException;
import com.example.demo.user.repository.UserRepository;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - User Service Test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

  @InjectMocks
  private UserServiceImpl userServiceImpl;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private final User user = Instancio.create(User.class);

  @Nested
  @DisplayName("Validate And Return User Entity Test")
  class ValidateReturnUserTest {

    @Test
    @DisplayName("Success validate and get user entity")
    public void should_AssertUserEntity_when_GivenUserId() {
      when(userRepository.findOneById(anyLong())).thenReturn(Optional.of(user));

      User validateUser = userServiceImpl.validateReturnUser(user.getId());

      assertNotNull(validateUser);
      assertEquals(user.getId(), validateUser.getId());
      assertEquals(user.getEmail(), validateUser.getEmail());
      assertEquals(user.getName(), validateUser.getName());
      assertEquals(user.getRole(), validateUser.getRole());
    }

    @Test
    @DisplayName("validate and user entity is not found exception")
    public void should_AssertUserNotFoundException_when_GivenUserId() {
      when(userRepository.findOneById(anyLong())).thenReturn(Optional.empty());

      assertThrows(
        UserNotFoundException.class,
        () -> userServiceImpl.validateReturnUser(user.getId())
      );
    }

    @Nested
    @DisplayName("Validate and authenticated Return User Entity")
    class validateAuthReturnUserTest {

      SignInRequest signInRequest = Instancio.create(SignInRequest.class);

      @Test
      @DisplayName("Success validate and authenticated get user entity")
      public void should_AssertUserEntity_when_GivenSignInRequest() {
        when(userRepository.findOneByEmail(anyString()))
          .thenReturn(Optional.of(user));

        when(
          user.validatePassword(
            signInRequest.getPassword(),
            bCryptPasswordEncoder
          )
        )
          .thenReturn(true);

        User validateAuthUser = userServiceImpl.validateAuthReturnUser(
          signInRequest
        );

        assertNotNull(validateAuthUser);
        assertEquals(user.getId(), validateAuthUser.getId());
        assertEquals(user.getEmail(), validateAuthUser.getEmail());
        assertEquals(user.getName(), validateAuthUser.getName());
        assertEquals(user.getRole(), validateAuthUser.getRole());
      }

      @Test
      @DisplayName("validate and authenticated user is not found exception")
      public void should_AssertUserNotFoundException_when_GivenSignInRequest() {
        when(userRepository.findOneByEmail(anyString()))
          .thenReturn(Optional.empty());

        assertThrows(
          UserNotFoundException.class,
          () -> userServiceImpl.validateAuthReturnUser(signInRequest)
        );
      }

      @Test
      @DisplayName("validate and authenticated user is unauthorized exception")
      public void should_AssertUserUnAuthorizedException_when_GivenSignInRequest() {
        when(userRepository.findOneByEmail(anyString()))
          .thenReturn(Optional.of(user));

        when(
          user.validatePassword(
            signInRequest.getPassword(),
            bCryptPasswordEncoder
          )
        )
          .thenReturn(false);

        assertThrows(
          UserUnAuthorizedException.class,
          () -> userServiceImpl.validateAuthReturnUser(signInRequest)
        );
      }
    }
  }
}
