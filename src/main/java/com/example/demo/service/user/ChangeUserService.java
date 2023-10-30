package com.example.demo.service.user;

import com.example.demo.domain.user.dto.serve.CreateUserRequest;
import com.example.demo.domain.user.dto.serve.UpdateMeResponse;
import com.example.demo.domain.user.dto.serve.UpdateUserRequest;
import com.example.demo.domain.user.dto.serve.UpdateUserResponse;
import com.example.demo.domain.user.entity.User;

public interface ChangeUserService {
  public UpdateUserResponse updateUser(Long userId, UpdateUserRequest dto);

  public UpdateMeResponse updateMe(Long userId, UpdateUserRequest dto);

  public User createUser(CreateUserRequest dto);

  public void deleteUser(Long userId);
}
