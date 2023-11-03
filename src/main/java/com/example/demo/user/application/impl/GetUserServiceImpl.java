package com.example.demo.user.application.impl;

import com.example.demo.user.application.GetUserService;
import com.example.demo.user.dto.serve.GetUserResponse;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetUserServiceImpl implements GetUserService {

  private final UserRepository userRepository;

  @Override
  public GetUserResponse getUserById(Long userId) {
    final User user = userRepository.findOneById(userId).orElse(null);

    if (Objects.isNull(user)) {
      return null;
    }

    return GetUserResponse.of(user);
  }

  @Override
  public GetUserResponse getUserByEmail(String email) {
    final User user = userRepository.findOneByEmail(email).orElse(null);

    if (Objects.isNull(user)) {
      return null;
    }

    return GetUserResponse.of(user);
  }

  @Override
  public List<GetUserResponse> getUsers(Pageable pageable) {
    return userRepository
      .findAll(pageable)
      .getContent()
      .stream()
      .map(GetUserResponse::new)
      .collect(Collectors.toList());
  }
}
