package com.example.demo.security.service;

import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.component.JWTProvider;
import com.example.demo.security.exception.TokenNotFoundException;
import com.example.demo.user.entity.User;
import com.example.demo.utils.RedisUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final JWTProvider jwtProvider;
  private final RedisUtils redisUtils;

  public String getRefreshToken(Long userId) {
    String redisKey = redisUtils.generateSessionKey(userId);
    String refreshToken = redisUtils.get(redisKey);

    if (Objects.isNull(refreshToken)) {
      throw new TokenNotFoundException("Refresh Token");
    }

    return refreshToken;
  }

  public void deleteRefreshToken(Long userId) {
    String redisKey = redisUtils.generateSessionKey(userId);
    redisUtils.delete(redisKey);
  }

  public String createAccessToken(User user) {
    return jwtProvider.createAccessToken(SecurityUserItem.of(user));
  }

  public String createRefreshToken(User user) {
    return jwtProvider.createRefreshToken(SecurityUserItem.of(user));
  }

  public String refreshAccessToken(SecurityUserItem securityUserItem) {
    return jwtProvider.refreshAccessToken(
      securityUserItem,
      getRefreshToken(securityUserItem.getUserId())
    );
  }

  public String createFullTokens(User user) {
    // Remove existing refresh tokens in advance
    deleteRefreshToken(user.getId());
    createRefreshToken(user);
    return createAccessToken(user);
  }
}
