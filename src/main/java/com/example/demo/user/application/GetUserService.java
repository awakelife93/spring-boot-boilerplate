package com.example.demo.user.application;

import com.example.demo.user.dto.serve.response.GetUserResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GetUserService {
  public GetUserResponse getUserById(Long userId);

  public GetUserResponse getUserByEmail(String email);

  public List<GetUserResponse> getUserList(Pageable pageable);
}
