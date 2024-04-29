package com.example.demo.user.dto.serve.response;

import com.example.demo.common.constant.UserRole;
import com.example.demo.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserResponse {

  @Schema(description = "User Id", nullable = false)
  private Long userId;

  @Schema(
    description = "User Role",
    nullable = false,
    implementation = UserRole.class
  )
  private UserRole role;

  @Schema(description = "User Name", nullable = false)
  private String name;

  @Schema(description = "User Email", nullable = false)
  private String email;

  @Schema(description = "User AccessToken", nullable = false)
  private String accessToken;

  @Builder
  public CreateUserResponse(User user, String accessToken) {
    this.userId = user.getId();
    this.role = user.getRole();
    this.name = user.getName();
    this.email = user.getEmail();
    this.accessToken = accessToken;
  }

  public static CreateUserResponse of(User user, String accessToken) {
    return CreateUserResponse
      .builder()
      .user(user)
      .accessToken(accessToken)
      .build();
  }
}
