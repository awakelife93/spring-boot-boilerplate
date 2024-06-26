package com.example.demo.user.entity;

import com.example.demo.common.entity.BaseSoftDeleteEntity;
import com.example.demo.post.entity.Post;
import com.example.demo.user.constant.UserRole;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@Table(name = "\"user\"")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(
  sql = "UPDATE \"user\" SET deleted_dt = CURRENT_TIMESTAMP WHERE user_id = ?"
)
@SQLRestriction("deleted_dt IS NULL")
public class User extends BaseSoftDeleteEntity {

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false, updatable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  private List<Post> posts;

  @Builder
  private User(
    @NonNull String name,
    @NonNull String email,
    @NonNull String password,
    @NonNull UserRole role
  ) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public User update(String name, UserRole role) {
    this.name = name;
    this.role = role;
    return this;
  }

  public User encodePassword(BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.password = bCryptPasswordEncoder.encode(this.password);
    return this;
  }

  public boolean validatePassword(
    String password,
    BCryptPasswordEncoder bCryptPasswordEncoder
  ) {
    return bCryptPasswordEncoder.matches(password, this.password);
  }

  public static User toEntity(
    String email,
    String name,
    String password,
    UserRole role
  ) {
    return User
      .builder()
      .email(email)
      .name(name)
      .password(password)
      .role(role)
      .build();
  }
}
