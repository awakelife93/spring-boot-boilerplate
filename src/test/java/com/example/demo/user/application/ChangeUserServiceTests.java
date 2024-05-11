package com.example.demo.user.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.security.service.TokenService;
import com.example.demo.user.application.impl.ChangeUserServiceImpl;
import com.example.demo.user.application.impl.GetUserServiceImpl;
import com.example.demo.user.application.impl.UserServiceImpl;
import com.example.demo.user.dto.serve.request.CreateUserRequest;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.dto.serve.response.CreateUserResponse;
import com.example.demo.user.dto.serve.response.GetUserResponse;
import com.example.demo.user.dto.serve.response.UpdateUserResponse;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.AlreadyUserExistException;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.repository.UserRepository;
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
@DisplayName("Unit - Post / Put / Delete / Patch User Service Test")
@ExtendWith(MockitoExtension.class)
public class ChangeUserServiceTests {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Mock
  private TokenService tokenService;

  @Mock
  private GetUserServiceImpl getUserServiceImpl;

  @Mock
  private UserServiceImpl userServiceImpl;

  @InjectMocks
  private ChangeUserServiceImpl changeUserServiceImpl;

  private final User user = Instancio.create(User.class);

  private final String defaultUserEncodePassword =
    "$2a$10$T44NRNpbxkQ9qHbCtqQZ7O3gYfipzC0cHvOIJ/aV4PTlvJjtDl7x2\n" + //
    "";

  private final String defaultAccessToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" + //
    "";

  @Nested
  @DisplayName("Delete User Test")
  class DeleteTest {

    @Test
    @DisplayName("Success delete user")
    public void should_VerifyCallDeleteRefreshTokenAndDeleteByIdMethods_when_GivenUserId() {
      changeUserServiceImpl.deleteUser(user.getId());

      verify(tokenService, times(1)).deleteRefreshToken(anyLong());
      verify(userRepository, times(1)).deleteById(anyLong());
    }
  }

  @Nested
  @DisplayName("Update User Test")
  class UpdateTest {

    UpdateUserRequest updateUserRequest = Instancio.create(
      UpdateUserRequest.class
    );

    @Test
    @DisplayName("Success update user")
    public void should_AssertUpdateUserResponse_when_GivenUserIdAndUpdateUserRequest() {
      when(userServiceImpl.validateReturnUser(anyLong())).thenReturn(user);

      UpdateUserResponse updateUserResponse = changeUserServiceImpl.updateUser(
        user.getId(),
        updateUserRequest
      );

      assertNotNull(updateUserResponse);
      assertEquals(user.getName(), updateUserResponse.getName());
      assertEquals(user.getRole(), updateUserResponse.getRole());
    }

    @Test
    @DisplayName("Not found user")
    public void should_AssertUserNotFoundException_when_GivenUserIdAndUpdateUserRequest() {
      when(userServiceImpl.validateReturnUser(anyLong()))
        .thenThrow(new UserNotFoundException(user.getId()));

      assertThrows(
        UserNotFoundException.class,
        () -> changeUserServiceImpl.updateUser(user.getId(), updateUserRequest)
      );
    }
  }

  @Nested
  @DisplayName("Create User Test")
  class RegisterTest {

    CreateUserRequest createUserRequest = Instancio.create(
      CreateUserRequest.class
    );

    @Test
    @DisplayName("Success create user")
    public void should_AssertCreateUserResponse_when_GivenCreateUserRequest() {
      when(bCryptPasswordEncoder.encode(anyString()))
        .thenReturn(defaultUserEncodePassword);
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(tokenService.createFullTokens(any(User.class)))
        .thenReturn(defaultAccessToken);

      CreateUserResponse createUserResponse = changeUserServiceImpl.createUser(
        createUserRequest
      );

      assertNotNull(createUserResponse);
      assertEquals(user.getEmail(), createUserResponse.getEmail());
      assertEquals(user.getName(), createUserResponse.getName());
    }

    @Test
    @DisplayName("Already user exist")
    public void should_AssertAlreadyUserExistException_when_GivenCreateUserRequest() {
      when(getUserServiceImpl.getUserByEmail(anyString()))
        .thenReturn(GetUserResponse.of(user));

      assertThrows(
        AlreadyUserExistException.class,
        () -> changeUserServiceImpl.createUser(createUserRequest)
      );
    }
  }
}
