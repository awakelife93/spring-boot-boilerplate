package com.example.demo.security.component.provider;

import com.example.demo.security.SecurityUserItem;
import com.example.demo.security.exception.RefreshTokenNotFoundException;
import com.example.demo.user.entity.User;
import com.example.demo.utils.RedisUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final JWTProvider jwtProvider;
  private final RedisUtils redisUtils;

  public String getRefreshToken(Long userId) {
    String redisKey = redisUtils.generateSessionKey(userId);
    String refreshToken = redisUtils.get(redisKey);

    if (Objects.isNull(refreshToken)) {
      throw new RefreshTokenNotFoundException(userId);
    }

    return refreshToken;
  }

  public void deleteRefreshToken(Long userId) {
    String redisKey = redisUtils.generateSessionKey(userId);
    redisUtils.delete(redisKey);
  }

  public void createRefreshToken(User user) {
    String redisKey = redisUtils.generateSessionKey(user.getId());
    redisUtils.set(
      redisKey,
      jwtProvider.createRefreshToken(SecurityUserItem.of(user)),
      jwtProvider.getRefreshExpireTime()
    );
  }

  public String createAccessToken(User user) {
    return jwtProvider.createAccessToken(SecurityUserItem.of(user));
  }

  public String refreshAccessToken(SecurityUserItem securityUserItem) {
    return jwtProvider.refreshAccessToken(
      securityUserItem,
      getRefreshToken(securityUserItem.getUserId())
    );
  }

  public String createFullTokens(User user) {
    createRefreshToken(user);
    return createAccessToken(user);
  }
}
