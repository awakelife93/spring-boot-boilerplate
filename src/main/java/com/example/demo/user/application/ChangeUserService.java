package com.example.demo.user.application;

import com.example.demo.user.dto.serve.CreateUserRequest;
import com.example.demo.user.dto.serve.UpdateMeResponse;
import com.example.demo.user.dto.serve.UpdateUserRequest;
import com.example.demo.user.dto.serve.UpdateUserResponse;
import com.example.demo.user.entity.User;

public interface ChangeUserService {
  public UpdateUserResponse updateUser(Long userId, UpdateUserRequest dto);

  public UpdateMeResponse updateMe(Long userId, UpdateUserRequest dto);

  public User createUser(CreateUserRequest dto);

  public void deleteUser(Long userId);
}
