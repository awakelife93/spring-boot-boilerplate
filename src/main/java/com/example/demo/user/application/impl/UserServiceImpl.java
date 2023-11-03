package com.example.demo.user.application.impl;

import com.example.demo.auth.dto.serve.SignInRequest;
import com.example.demo.user.application.UserService;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.exception.UserUnAuthorizedException;
import com.example.demo.user.repository.user.UserRepository;
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
