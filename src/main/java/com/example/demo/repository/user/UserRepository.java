package com.example.demo.repository.user;

import com.example.demo.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
  extends JpaRepository<User, Long>, CustomUserRepository {
  Optional<User> findOneById(Long userId);

  Optional<User> findOneByEmail(String email);
}
