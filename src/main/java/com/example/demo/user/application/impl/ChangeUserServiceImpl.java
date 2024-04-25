package com.example.demo.user.application.impl;

import com.example.demo.auth.exception.AlreadyUserExistException;
import com.example.demo.security.service.TokenService;
import com.example.demo.user.application.ChangeUserService;
import com.example.demo.user.application.GetUserService;
import com.example.demo.user.application.UserService;
import com.example.demo.user.dto.serve.request.CreateUserRequest;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.dto.serve.response.CreateUserResponse;
import com.example.demo.user.dto.serve.response.GetUserResponse;
import com.example.demo.user.dto.serve.response.UpdateMeResponse;
import com.example.demo.user.dto.serve.response.UpdateUserResponse;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangeUserServiceImpl implements ChangeUserService {

  private final TokenService tokenService;
  private final UserService userService;
  private final GetUserService getUserService;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public CreateUserResponse createUser(CreateUserRequest dto) {
    final GetUserResponse confirmUser = getUserService.getUserByEmail(
      dto.getEmail()
    );

    if (!Objects.isNull(confirmUser)) {
      throw new AlreadyUserExistException();
    }

    final User user = User
      .toEntity(dto.getEmail(), dto.getName(), dto.getPassword(), null)
      .encodePassword(bCryptPasswordEncoder);

    return CreateUserResponse.of(
      userRepository.save(user),
      tokenService.createFullTokens(user)
    );
  }

  @Override
  public UpdateUserResponse updateUser(Long userId, UpdateUserRequest dto) {
    final User user = userService.validateReturnUser(userId).update(dto);
    return UpdateUserResponse.of(user);
  }

  @Override
  public UpdateMeResponse updateMe(Long userId, UpdateUserRequest dto) {
    final User user = userService.validateReturnUser(userId).update(dto);

    return UpdateMeResponse.of(user, tokenService.createFullTokens(user));
  }

  @Override
  public void deleteUser(Long userId) {
    tokenService.deleteRefreshToken(userId);
    userRepository.deleteById(userId);
  }
}
