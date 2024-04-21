package com.example.demo.auth.dto.serve.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshAccessTokenResponse {

  private String accessToken;

  @Builder
  public RefreshAccessTokenResponse(String accessToken) {
    this.accessToken = accessToken;
  }

  public static RefreshAccessTokenResponse of(String accessToken) {
    return RefreshAccessTokenResponse
      .builder()
      .accessToken(accessToken)
      .build();
  }
}
