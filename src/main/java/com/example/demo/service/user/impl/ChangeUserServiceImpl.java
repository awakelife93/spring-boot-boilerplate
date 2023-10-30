package com.example.demo.service.user.impl;

import com.example.demo.domain.user.dto.serve.CreateUserRequest;
import com.example.demo.domain.user.dto.serve.UpdateMeResponse;
import com.example.demo.domain.user.dto.serve.UpdateUserRequest;
import com.example.demo.domain.user.dto.serve.UpdateUserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.security.service.TokenService;
import com.example.demo.service.user.ChangeUserService;
import com.example.demo.service.user.UserService;
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
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public User createUser(CreateUserRequest dto) {
    final User user = dto.toEntity().encodePassword(bCryptPasswordEncoder);
    return userRepository.save(user);
  }

  @Override
  public UpdateUserResponse updateUser(Long userId, UpdateUserRequest dto) {
    final User user = userService.validateReturnUser(userId).update(dto);
    return UpdateUserResponse.builder().user(user).build();
  }

  @Override
  public UpdateMeResponse updateMe(Long userId, UpdateUserRequest dto) {
    final User user = userService.validateReturnUser(userId).update(dto);

    return UpdateMeResponse
      .builder()
      .user(user)
      .accessToken(tokenService.createFullTokens(user))
      .build();
  }

  @Override
  public void deleteUser(Long userId) {
    tokenService.deleteRefreshToken(userId);
    userRepository.deleteById(userId);
  }
}
