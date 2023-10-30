package com.example.demo.service.user;

import com.example.demo.domain.auth.dto.serve.SignInRequest;
import com.example.demo.domain.user.entity.User;

public interface UserService {
  public User validateReturnUser(Long userId);

  public User validateAuthReturnUser(SignInRequest dto);
}
