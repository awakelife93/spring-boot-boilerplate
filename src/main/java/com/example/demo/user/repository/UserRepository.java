package com.example.demo.user.repository;

import com.example.demo.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository
  extends JpaRepository<User, Long>, CustomUserRepository {
  Optional<User> findOneById(Long userId);

  Optional<User> findOneByEmail(String email);

  @Query(
    value = "SELECT * FROM \"user\"  WHERE (deleted_dt + INTERVAL '1 YEAR') <= ?",
    nativeQuery = true
  )
  List<User> findDeletedUsersYearAgo(LocalDateTime localDateTime);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query(value = "DELETE FROM \"user\" WHERE user_id = ?", nativeQuery = true)
  void hardDeleteUser(Long userId);
}
