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
  public void saveRefreshToken(String tokenId, String refreshToken, String userId, long expired) {
    String key = "RT:" + refreshToken;
    redisTemplate.opsForValue().set(
        key,
        String.format("%s:%s", userId, tokenId),
        Duration.ofSeconds(expired)
    );
  }
}
