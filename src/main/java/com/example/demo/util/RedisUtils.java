package com.example.demo.util;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtils {

  private final StringRedisTemplate stringRedisTemplate;

  public String get(String key) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void set(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    Duration expireDuration = Duration.ofMillis(duration);
    valueOperations.set(key, value, expireDuration);
  }

  public void delete(String key) {
    stringRedisTemplate.delete(key);
  }

  public String generateSessionKey(Long userId) {
    final String prefix = "Session_";
    return prefix + userId;
  }
}
