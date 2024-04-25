package com.example.demo.auth.api;

import com.example.demo.auth.application.AuthService;
import com.example.demo.auth.dto.serve.request.SignInRequest;
import com.example.demo.auth.dto.serve.response.RefreshAccessTokenResponse;
import com.example.demo.auth.dto.serve.response.SignInResponse;
import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @PostMapping("/signIn")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<SignInResponse> signIn(
    @RequestBody @Valid SignInRequest dto
  ) {
    final SignInResponse signInResponse = authService.signIn(dto);
    return ResponseEntity.ok(signInResponse);
  }

  @PostMapping("/signOut")
  @ResponseStatus(HttpStatus.OK)
  public void signOut(@CurrentUser SecurityUserItem securityUserItem) {
    authService.signOut(securityUserItem.getUserId());
  }

  @PostMapping("/refresh")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<RefreshAccessTokenResponse> refreshAccessToken(
    @CurrentUser SecurityUserItem securityUserItem
  ) {
    final RefreshAccessTokenResponse refreshAccessToken = authService.refreshAccessToken(
      securityUserItem
    );
    return ResponseEntity.ok(refreshAccessToken);
  }
}
