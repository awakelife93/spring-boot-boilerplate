package com.example.demo.domain.user.dto.serve;

import com.example.demo.common.annotaction.ValidEnum;
import com.example.demo.common.constant.UserRole;
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
  private String name;

  @ValidEnum(enumClass = UserRole.class, message = "field role is invalid")
  private UserRole role;
}
