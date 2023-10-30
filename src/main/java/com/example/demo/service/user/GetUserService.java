package com.example.demo.service.user;

import com.example.demo.domain.user.dto.serve.GetUserResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GetUserService {
  public GetUserResponse getUserById(Long userId);

  public GetUserResponse getUserByEmail(String email);

  public List<GetUserResponse> getUsers(Pageable pageable);
}
