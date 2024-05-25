package com.example.demo.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.common.config.JpaAuditConfig;
import com.example.demo.common.config.QueryDslConfig;
import com.example.demo.user.constant.UserRole;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.entity.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - User Repository Test")
@Import(value = { QueryDslConfig.class, JpaAuditConfig.class })
@DataJpaTest
public class UserRepositoryTests {

  @Autowired
  private UserRepository userRepository;

  private User userEntity;

  private final String defaultUserEmail = "awakelife93@gmail.com";

  private final String defaultUserPassword = "test_password_123!@";

  private final String defaultUserName = "Hyunwoo Park";

  private final UserRole defaultUserRole = UserRole.USER;

  @BeforeEach
  public void setUp() throws Exception {
    userEntity =
      User.toEntity(
        defaultUserEmail,
        defaultUserName,
        defaultUserPassword,
        defaultUserRole
      );
  }

  @Test
  @DisplayName("Create user")
  void should_AssertCreatedUserEntity_when_GivenUserEntity() {
    User createUser = userRepository.save(userEntity);

    assertEquals(createUser.getId(), userEntity.getId());
    assertEquals(createUser.getEmail(), userEntity.getEmail());
    assertEquals(createUser.getName(), userEntity.getName());
    assertEquals(createUser.getRole(), userEntity.getRole());
  }

  @Test
  @DisplayName("Update user")
  void should_AssertUpdatedUserEntity_when_GivenUserIdAndUpdateUserRequest() {
    UpdateUserRequest updateUserRequest = Instancio.create(
      UpdateUserRequest.class
    );

    User beforeUpdateUser = userRepository.save(userEntity);

    userRepository.save(
      beforeUpdateUser.update(
        updateUserRequest.getName(),
        updateUserRequest.getRole()
      )
    );

    User afterUpdateUser = userRepository
      .findOneById(beforeUpdateUser.getId())
      .get();

    assertEquals(afterUpdateUser.getName(), updateUserRequest.getName());
    assertEquals(afterUpdateUser.getRole(), updateUserRequest.getRole());
  }

  @Test
  @DisplayName("Delete user")
  void should_AssertDeletedUserEntity_when_GivenUserId() {
    User beforeDeleteUser = userRepository.save(userEntity);

    userRepository.deleteById(beforeDeleteUser.getId());

    User afterDeleteUser = userRepository
      .findOneById(beforeDeleteUser.getId())
      .orElse(null);

    assertNull(afterDeleteUser);
  }

  @Test
  @DisplayName("Find user by id")
  void should_AssertFindUserEntity_when_GivenUserId() {
    User beforeFindUser = userRepository.save(userEntity);

    User afterFindUser = userRepository
      .findOneById(beforeFindUser.getId())
      .get();

    assertEquals(beforeFindUser.getId(), afterFindUser.getId());
    assertEquals(beforeFindUser.getEmail(), afterFindUser.getEmail());
    assertEquals(beforeFindUser.getName(), afterFindUser.getName());
    assertEquals(beforeFindUser.getRole(), afterFindUser.getRole());
  }

  @Test
  @DisplayName("Find user by email")
  void should_AssertFindUserEntity_when_GivenUserEmail() {
    User beforeFindUser = userRepository.save(userEntity);

    User afterFindUser = userRepository
      .findOneByEmail(beforeFindUser.getEmail())
      .get();

    assertEquals(beforeFindUser.getId(), afterFindUser.getId());
    assertEquals(beforeFindUser.getEmail(), afterFindUser.getEmail());
    assertEquals(beforeFindUser.getName(), afterFindUser.getName());
    assertEquals(beforeFindUser.getRole(), afterFindUser.getRole());
  }
}
