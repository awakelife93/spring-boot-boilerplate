package com.example.demo.auth.api;

import com.example.demo.auth.application.AuthService;
import com.example.demo.auth.dto.serve.SignInRequest;
import com.example.demo.auth.dto.serve.SignInResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.annotation.CurrentUser;
import com.example.demo.user.dto.serve.CreateUserRequest;
import com.example.demo.user.dto.serve.CreateUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signUp")
  @ResponseStatus(HttpStatus.CREATED)
  public CreateUserResponse signUp(@RequestBody @Valid CreateUserRequest dto) {
    return authService.signUp(dto);
  }

  @PostMapping("/signIn")
  @ResponseStatus(HttpStatus.OK)
  public SignInResponse signIn(@RequestBody @Valid SignInRequest dto) {
    return authService.signIn(dto);
  }

  @PostMapping("/signOut")
  @ResponseStatus(HttpStatus.OK)
  public void signOut(@CurrentUser SecurityUserItem securityUserItem) {
    authService.signOut(securityUserItem.getUserId());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String refreshAccessToken(
    @CurrentUser SecurityUserItem securityUserItem
  ) {
    return authService.refreshAccessToken(securityUserItem);
  }
}
