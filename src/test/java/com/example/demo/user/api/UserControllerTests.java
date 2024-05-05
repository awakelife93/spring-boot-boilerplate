package com.example.demo.user.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.security.SecurityUserItem;
import com.example.demo.user.application.impl.ChangeUserServiceImpl;
import com.example.demo.user.application.impl.GetUserServiceImpl;
import com.example.demo.user.dto.serve.request.CreateUserRequest;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.dto.serve.response.CreateUserResponse;
import com.example.demo.user.dto.serve.response.GetUserResponse;
import com.example.demo.user.dto.serve.response.UpdateMeResponse;
import com.example.demo.user.dto.serve.response.UpdateUserResponse;
import com.example.demo.user.entity.User;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - User Controller Test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

  @InjectMocks
  private UserController userController;

  @Mock
  private GetUserServiceImpl getUserServiceImpl;

  @Mock
  private ChangeUserServiceImpl changeUserServiceImpl;

  private final User user = Instancio.create(User.class);

  private final String defaultAccessToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" + //
    "";

  private final Pageable defaultPageable = Pageable.ofSize(1);

  @Test
  @DisplayName("Get user by id")
  public void should_AssertGetUserResponse_when_GivenUserId() {
    when(getUserServiceImpl.getUserById(anyLong()))
      .thenReturn(GetUserResponse.of(user));

    ResponseEntity<GetUserResponse> response = userController.getUserById(
      user.getId()
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    GetUserResponse body = response.getBody();
    assertEquals(user.getId(), body.getUserId());
    assertEquals(user.getEmail(), body.getEmail());
    assertEquals(user.getName(), body.getName());
    assertEquals(user.getRole(), body.getRole());
  }

  @Test
  @DisplayName("Get user list")
  public void should_AssertListOfGetUserResponse_when_GivenDefaultPageable() {
    when(getUserServiceImpl.getUserList(any(Pageable.class)))
      .thenReturn(List.of(GetUserResponse.of(user)));

    ResponseEntity<List<GetUserResponse>> response = userController.getUserList(
      defaultPageable
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    List<GetUserResponse> body = response.getBody();
    assertThat(body).isNotEmpty();
    assertEquals(user.getId(), body.get(0).getUserId());
    assertEquals(user.getEmail(), body.get(0).getEmail());
    assertEquals(user.getName(), body.get(0).getName());
    assertEquals(user.getRole(), body.get(0).getRole());
  }

  @Test
  @DisplayName("Create user")
  public void should_AssertCreateUserResponse_when_GivenCreateUserRequest() {
    CreateUserRequest createUserRequest = Instancio.create(
      CreateUserRequest.class
    );

    when(changeUserServiceImpl.createUser(any(CreateUserRequest.class)))
      .thenReturn(CreateUserResponse.of(user, defaultAccessToken));

    ResponseEntity<CreateUserResponse> response = userController.createUser(
      createUserRequest
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    CreateUserResponse body = response.getBody();
    assertEquals(user.getEmail(), body.getEmail());
    assertEquals(user.getName(), body.getName());
    assertEquals(defaultAccessToken, body.getAccessToken());
  }

  @Test
  @DisplayName("Update user")
  public void should_AssertUpdateUserResponse_when_GivenUserIdAndUpdateUserRequest() {
    UpdateUserRequest updateUserRequest = Instancio.create(
      UpdateUserRequest.class
    );

    when(
      changeUserServiceImpl.updateUser(anyLong(), any(UpdateUserRequest.class))
    )
      .thenReturn(UpdateUserResponse.of(user));

    ResponseEntity<UpdateUserResponse> response = userController.updateUser(
      updateUserRequest,
      user.getId()
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    UpdateUserResponse body = response.getBody();
    assertEquals(user.getEmail(), body.getEmail());
    assertEquals(user.getName(), body.getName());
    assertEquals(user.getRole(), body.getRole());
  }

  @Test
  @DisplayName("Update me")
  public void should_AssertUpdateMeResponse_when_GivenSecurityUserItemAndUpdateUserRequest() {
    UpdateUserRequest updateUserRequest = Instancio.create(
      UpdateUserRequest.class
    );
    SecurityUserItem securityUserItem = Instancio.create(
      SecurityUserItem.class
    );

    when(
      changeUserServiceImpl.updateMe(anyLong(), any(UpdateUserRequest.class))
    )
      .thenReturn(UpdateMeResponse.of(user, defaultAccessToken));

    ResponseEntity<UpdateMeResponse> response = userController.updateMe(
      updateUserRequest,
      securityUserItem
    );

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    UpdateMeResponse body = response.getBody();
    assertEquals(user.getEmail(), body.getEmail());
    assertEquals(user.getName(), body.getName());
    assertEquals(user.getRole(), body.getRole());
    assertEquals(defaultAccessToken, body.getAccessToken());
  }

  @Test
  @DisplayName("Delete user")
  public void should_VerifyCallDeleteUserMethod_when_GivenUserId() {
    ResponseEntity<Void> response = userController.deleteUser(user.getId());

    assertNotNull(response);
    assertNull(response.getBody());
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    verify(changeUserServiceImpl, times(1)).deleteUser(anyLong());
  }
}
