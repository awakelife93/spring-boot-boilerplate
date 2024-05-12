package com.example.demo.user.dto.serve.request;

import com.example.demo.common.annotaction.ValidEnum;
import com.example.demo.user.constant.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserRequest {

  @NotBlank(message = "field name is blank")
  @Schema(description = "User Name", nullable = false)
  private String name;

  @ValidEnum(enumClass = UserRole.class, message = "field role is invalid")
  @Schema(
    description = "User Role",
    nullable = false,
    implementation = UserRole.class
  )
  private UserRole role;
}
