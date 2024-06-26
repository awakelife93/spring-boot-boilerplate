package com.example.demo.auth.dto.serve.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {

  @NotBlank(message = "field email is blank")
  @Email(message = "field email is not email format")
  @Schema(description = "User Email", nullable = false)
  private String email;

  @NotBlank(message = "field password is blank")
  @Size(
    min = 8,
    max = 20,
    message = "field password is min size 8 and max size 20"
  )
  @Schema(description = "User Password", nullable = false)
  private String password;
}
