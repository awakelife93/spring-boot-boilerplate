package com.example.demo.user.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.common.security.SecurityItem;
import com.example.demo.common.security.WithMockCustomUser;
import com.example.demo.security.service.TokenService;
import com.example.demo.user.application.impl.ChangeUserServiceImpl;
import com.example.demo.user.application.impl.GetUserServiceImpl;
import com.example.demo.user.dto.serve.request.CreateUserRequest;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.dto.serve.response.CreateUserResponse;
import com.example.demo.user.dto.serve.response.GetUserResponse;
import com.example.demo.user.dto.serve.response.UpdateMeResponse;
import com.example.demo.user.dto.serve.response.UpdateUserResponse;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.AlreadyUserExistException;
import com.example.demo.user.exception.UserNotFoundException;
import java.util.List;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("test")
@Tag("integration-test")
@DisplayName("integration - User Controller Test")
@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserIntegrationControllerTests extends SecurityItem {

  @MockBean
  private TokenService tokenService;

  @MockBean
  private GetUserServiceImpl getUserServiceImpl;

  @MockBean
  private ChangeUserServiceImpl changeUserServiceImpl;

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
  @DisplayName("GET /api/v1/users/{userId} Test")
  class GetUserByIdTest {

    @Test
    @DisplayName("GET /api/v1/users/{userId} Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToGetUserResponse_when_GivenUserIdAndUserIsAuthenticated()
      throws Exception {
      when(getUserServiceImpl.getUserById(anyLong()))
        .thenReturn(GetUserResponse.of(user));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/users/{userId}", user.getId())
            .with(csrf())
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
    @DisplayName("Not Found Exception GET /api/v1/users/{userId} Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToUserNotFoundException_when_GivenUserIdAndUserIsAuthenticated()
      throws Exception {
      UserNotFoundException userNotFoundException = new UserNotFoundException(
        user.getId()
      );

      when(getUserServiceImpl.getUserById(anyLong()))
        .thenThrow(userNotFoundException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/users/{userId}", user.getId())
            .with(csrf())
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

    @Test
    @DisplayName("Unauthorized Exception GET /api/v1/users/{userId} Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenUserIdAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/users/{userId}", user.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("GET /api/v1/users Test")
  class GetUserListTest {

    @Test
    @DisplayName("GET /api/v1/users Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToListOfGetUserResponse_when_GivenDefaultPageable()
      throws Exception {
      when(getUserServiceImpl.getUserList(any(Pageable.class)))
        .thenReturn(List.of(GetUserResponse.of(user)));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/users")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.[0].userId").value(user.getId()))
        .andExpect(jsonPath("$.data.[0].email").value(user.getEmail()))
        .andExpect(jsonPath("$.data.[0].name").value(user.getName()))
        .andExpect(jsonPath("$.data.[0].role").value(user.getRole().name()));
    }

    @Test
    @DisplayName("Empty GET /api/v1/users Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToListOfGetUserResponseIsEmpty_when_GivenDefaultPageable()
      throws Exception {
      when(getUserServiceImpl.getUserList(any(Pageable.class)))
        .thenReturn(List.of());

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .get("/api/v1/users")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data").isEmpty());
    }
  }

  @Nested
  @DisplayName("POST /api/v1/users/register Test")
  class CreateUserTest {

    CreateUserRequest createUserRequest = Instancio
      .of(CreateUserRequest.class)
      .set(Select.field(CreateUserRequest::getEmail), defaultUserEmail)
      .set(Select.field(CreateUserRequest::getPassword), defaultUserPassword)
      .create();

    @Test
    @DisplayName("POST /api/v1/users/register Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToCreateUserResponse_when_GivenCreateUserRequest()
      throws Exception {
      when(changeUserServiceImpl.createUser(any(CreateUserRequest.class)))
        .thenReturn(CreateUserResponse.of(user, defaultAccessToken));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/users/register")
            .with(csrf())
            .content(objectMapper.writeValueAsString(createUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage))
        .andExpect(jsonPath("$.data.userId").value(user.getId()))
        .andExpect(jsonPath("$.data.email").value(user.getEmail()))
        .andExpect(jsonPath("$.data.name").value(user.getName()))
        .andExpect(jsonPath("$.data.role").value(user.getRole().name()))
        .andExpect(jsonPath("$.data.accessToken").value(defaultAccessToken));
    }

    @Test
    @DisplayName("Field Valid Exception POST /api/v1/users/register Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToValidException_when_GivenWrongCreateUserRequest()
      throws Exception {
      CreateUserRequest createUserRequest = Instancio
        .of(CreateUserRequest.class)
        .set(Select.field(CreateUserRequest::getEmail), "wrong_email_format")
        .set(Select.field(CreateUserRequest::getPassword), "1234")
        .create();

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/users/register")
            .with(csrf())
            .content(objectMapper.writeValueAsString(createUserRequest))
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
    @DisplayName("Already Exist Exception POST /api/v1/users/register Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToAlreadyUserExistException_when_GivenCreateUserRequest()
      throws Exception {
      AlreadyUserExistException alreadyUserExistException = new AlreadyUserExistException(
        createUserRequest.getEmail()
      );

      when(changeUserServiceImpl.createUser(any(CreateUserRequest.class)))
        .thenThrow(alreadyUserExistException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/v1/users/register")
            .with(csrf())
            .content(objectMapper.writeValueAsString(createUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isConflict())
        .andExpect(
          jsonPath("$.code").value(alreadyUserExistException.getCode().value())
        )
        .andExpect(
          jsonPath("$.message").value(alreadyUserExistException.getMessage())
        )
        .andExpect(jsonPath("$.errors").isEmpty());
    }
  }

  @Nested
  @DisplayName("PATCH /api/v1/users/{userId} Test")
  class UpdateUserTest {

    UpdateUserRequest updateUserRequest = Instancio.create(
      UpdateUserRequest.class
    );

    @Test
    @DisplayName("PATCH /api/v1/users/{userId} Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToUpdateUserResponse_when_GivenUserIdAndUpdateUserRequestAndUserIsAuthenticated()
      throws Exception {
      when(
        changeUserServiceImpl.updateUser(
          anyLong(),
          any(UpdateUserRequest.class)
        )
      )
        .thenReturn(UpdateUserResponse.of(user));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users/{userId}", user.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
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
    @DisplayName("Field Valid Exception PATCH /api/v1/users/{userId} Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToValidException_when_GivenUserIdAndWrongUpdateUserRequestAndUserIsAuthenticated()
      throws Exception {
      UpdateUserRequest updateUserRequest = Instancio
        .of(UpdateUserRequest.class)
        .set(Select.field(UpdateUserRequest::getName), null)
        .set(Select.field(UpdateUserRequest::getRole), null)
        .create();

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users/{userId}", user.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        // name:field name is blank, role:field role is invalid,
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Unauthorized Exception PATCH /api/v1/users/{userId} Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenUserIdAndUpdateUserRequestAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users/{userId}", user.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Not Found Exception PATCH /api/v1/users/{userId} Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToUserNotFoundException_when_GivenUserIdAndUpdateUserRequestAndUserIsAuthenticated()
      throws Exception {
      UserNotFoundException userNotFoundException = new UserNotFoundException(
        user.getId()
      );

      when(
        changeUserServiceImpl.updateUser(
          anyLong(),
          any(UpdateUserRequest.class)
        )
      )
        .thenThrow(userNotFoundException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users/{userId}", user.getId())
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
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
  @DisplayName("PATCH /api/v1/users Test")
  class UpdateMeTest {

    UpdateUserRequest updateUserRequest = Instancio.create(
      UpdateUserRequest.class
    );

    @Test
    @DisplayName("PATCH /api/v1/users Response")
    @WithMockCustomUser
    public void should_ExpectOKResponseToUpdateMeResponse_when_GivenSecurityUserItemAndUpdateUserRequestAndUserIsAuthenticated()
      throws Exception {
      when(
        changeUserServiceImpl.updateMe(anyLong(), any(UpdateUserRequest.class))
      )
        .thenReturn(UpdateMeResponse.of(user, defaultAccessToken));

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users")
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
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
    @DisplayName("Field Valid Exception PATCH /api/v1/users Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToValidException_when_GivenSecurityUserItemAndWrongUpdateUserRequestAndUserIsAuthenticated()
      throws Exception {
      UpdateUserRequest updateUserRequest = Instancio
        .of(UpdateUserRequest.class)
        .set(Select.field(UpdateUserRequest::getName), null)
        .set(Select.field(UpdateUserRequest::getRole), null)
        .create();

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users")
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        // name:field name is blank, role:field role is invalid,
        .andExpect(jsonPath("$.message").isString())
        .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Unauthorized Exception PATCH /api/v1/users Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenUpdateUserRequestAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users")
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Not Found Exception PATCH /api/v1/users Response")
    @WithMockCustomUser
    public void should_ExpectErrorResponseToUserNotFoundException_when_GivenSecurityUserItemAndUpdateUserRequestAndUserIsAuthenticated()
      throws Exception {
      UserNotFoundException userNotFoundException = new UserNotFoundException(
        user.getId()
      );

      when(
        changeUserServiceImpl.updateMe(anyLong(), any(UpdateUserRequest.class))
      )
        .thenThrow(userNotFoundException);

      mockMvc
        .perform(
          MockMvcRequestBuilders
            .patch("/api/v1/users")
            .with(csrf())
            .content(objectMapper.writeValueAsString(updateUserRequest))
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
  @DisplayName("DELETE /api/v1/users/{userId} Test")
  class DeleteUserTest {

    @Test
    @DisplayName("Delete /api/v1/users Response")
    @WithMockCustomUser
    public void should_ExpectOKResponse_when_GivenUserIdAndUserIsAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .delete("/api/v1/users/{userId}", user.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$.code").value(commonStatus))
        .andExpect(jsonPath("$.message").value(commonMessage));
    }

    @Test
    @DisplayName("Unauthorized Error DELETE /api/v1/users/{userId} Response")
    public void should_ExpectErrorResponseToUnauthorizedException_when_GivenUserIdAndUserIsNotAuthenticated()
      throws Exception {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .delete("/api/v1/users/{userId}", user.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }
  }
}
