package com.example.demo.user.application;

import com.example.demo.user.dto.serve.request.CreateUserRequest;
import com.example.demo.user.dto.serve.request.UpdateUserRequest;
import com.example.demo.user.dto.serve.response.CreateUserResponse;
import com.example.demo.user.dto.serve.response.UpdateMeResponse;
import com.example.demo.user.dto.serve.response.UpdateUserResponse;

public interface ChangeUserService {
  public UpdateUserResponse updateUser(Long userId, UpdateUserRequest dto);

  public UpdateMeResponse updateMe(Long userId, UpdateUserRequest dto);

  public CreateUserResponse createUser(CreateUserRequest dto);

  public void deleteUser(Long userId);
}
