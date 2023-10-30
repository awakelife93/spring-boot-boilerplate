package com.example.demo.service.user.impl;

import com.example.demo.domain.auth.dto.serve.SignInRequest;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.exception.UserNotFoundException;
import com.example.demo.domain.user.exception.UserUnAuthorizedException;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public User validateReturnUser(Long userId) {
    final User user = userRepository
      .findOneById(userId)
      .orElseThrow(() -> new UserNotFoundException());

    return user;
  }

  @Override
  public User validateAuthReturnUser(SignInRequest dto) {
    final User user = userRepository
      .findOneByEmail(dto.getEmail())
      .orElseThrow(() -> new UserNotFoundException());

    boolean isValidate = user.validatePassword(
      dto.getPassword(),
      bCryptPasswordEncoder
    );

    if (!isValidate) {
      throw new UserUnAuthorizedException();
    }

    return user;
  }
}
