package com.example.demo.user.application;

import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.user.entity.User;

public interface UserService {
  public User validateReturnUser(Long userId);

  public User validateAuthReturnUser(SignInRequest signInRequest);
}
