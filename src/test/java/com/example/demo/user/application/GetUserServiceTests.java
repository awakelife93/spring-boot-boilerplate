package com.example.demo.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.demo.user.application.impl.GetUserServiceImpl;
import com.example.demo.user.dto.serve.response.GetUserResponse;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.repository.UserRepository;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Get User Service Test")
@ExtendWith(MockitoExtension.class)
public class GetUserServiceTests {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private GetUserServiceImpl getUserServiceImpl;

  private final User user = Instancio.create(User.class);

  private final Pageable defaultPageable = Pageable.ofSize(1);

  @Nested
  @DisplayName("Get User By Id Test")
  class GetUserByIdTest {

    @Test
    @DisplayName("Success get user by id")
    public void should_AssertGetUserResponse_when_GivenUserId() {
      when(userRepository.findOneById(anyLong())).thenReturn(Optional.of(user));

      GetUserResponse getUserResponse = getUserServiceImpl.getUserById(
        user.getId()
      );

      assertNotNull(getUserResponse);
      assertEquals(user.getId(), getUserResponse.getUserId());
      assertEquals(user.getEmail(), getUserResponse.getEmail());
      assertEquals(user.getName(), getUserResponse.getName());
      assertEquals(user.getRole(), getUserResponse.getRole());
    }

    @Test
    @DisplayName("Not found user")
    public void should_AssertUserNotFoundException_when_GivenUserId() {
      when(userRepository.findOneById(anyLong()))
        .thenThrow(new UserNotFoundException(user.getId()));

      assertThrows(
        UserNotFoundException.class,
        () -> getUserServiceImpl.getUserById(user.getId())
      );
    }
  }

  @Nested
  @DisplayName("Get User By Email Test")
  class GetUserByEmailTest {

    @Test
    @DisplayName("Success get user by email")
    public void should_AssertGetUserResponse_when_GivenUserEmail() {
      when(userRepository.findOneByEmail(anyString()))
        .thenReturn(Optional.of(user));

      GetUserResponse getUserResponse = getUserServiceImpl.getUserByEmail(
        user.getEmail()
      );

      assertNotNull(getUserResponse);
      assertEquals(user.getId(), getUserResponse.getUserId());
      assertEquals(user.getEmail(), getUserResponse.getEmail());
      assertEquals(user.getName(), getUserResponse.getName());
      assertEquals(user.getRole(), getUserResponse.getRole());
    }

    @Test
    @DisplayName("Get user by email is null")
    public void should_AssertGetUserResponseIsNull_when_GivenUserEmail() {
      when(userRepository.findOneByEmail(anyString()))
        .thenReturn(Optional.empty());

      GetUserResponse getUserResponse = getUserServiceImpl.getUserByEmail(
        user.getEmail()
      );

      assertNull(getUserResponse);
    }
  }

  @Nested
  @DisplayName("Get User List Test")
  class GetUserListTest {

    @Test
    @DisplayName("Success get user list")
    public void should_AssertListOfGetUserResponse_when_GivenDefaultPageable() {
      Page<User> userList = new PageImpl<>(List.of(user));
      when(userRepository.findAll(any(Pageable.class))).thenReturn(userList);

      List<GetUserResponse> getUserResponseList = getUserServiceImpl.getUserList(
        defaultPageable
      );

      assertThat(getUserResponseList).isNotEmpty();
      assertEquals(getUserResponseList.get(0).getEmail(), user.getEmail());
      assertEquals(getUserResponseList.get(0).getName(), user.getName());
      assertEquals(getUserResponseList.get(0).getRole(), user.getRole());
    }

    @Test
    @DisplayName("Get user list is empty")
    public void should_AssertListOfGetUserResponseIsEmpty_when_GivenDefaultPageable() {
      Page<User> emptyUserList = new PageImpl<>(List.of());

      when(userRepository.findAll(any(Pageable.class)))
        .thenReturn(emptyUserList);

      List<GetUserResponse> getUserResponseList = getUserServiceImpl.getUserList(
        defaultPageable
      );

      assertThat(getUserResponseList).isEmpty();
      assertEquals(getUserResponseList.size(), 0);
    }
  }
}
