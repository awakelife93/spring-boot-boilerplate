package com.example.demo.user.application;

import com.example.demo.user.dto.serve.request.CreateUserRequest;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.dto.serve.response.UpdateMeResponse;
import com.example.demo.user.dto.serve.response.UpdateUserResponse;
import com.example.demo.user.entity.User;

public interface ChangeUserService {
  public UpdateUserResponse updateUser(Long userId, UpdateUserRequest dto);

  public UpdateMeResponse updateMe(Long userId, UpdateUserRequest dto);

  public User createUser(CreateUserRequest dto);

  public void deleteUser(Long userId);
}
