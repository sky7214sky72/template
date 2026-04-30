package com.infrastructure.auth.persistence;

import com.core.auth.application.port.out.SaveRefreshTokenPort;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenAdapter implements SaveRefreshTokenPort {

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void saveRefreshToken(String refreshToken, String userId, long expired) {
    String key = "RT:" + userId;
    redisTemplate.opsForValue().set(
        key,
        refreshToken,
        Duration.ofSeconds(expired)
    );
  }
}
