package com.infrastructure.auth.persistence;

import com.core.auth.application.port.out.DeleteRefreshTokenPort;
import com.core.auth.application.port.out.LoadRefreshTokenPort;
import com.core.auth.application.port.out.SaveRefreshTokenPort;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenAdapter implements SaveRefreshTokenPort, LoadRefreshTokenPort,
    DeleteRefreshTokenPort {

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

  @Override
  public void deleteRefreshToken(String refreshToken) {
    String key = "RT:" + refreshToken;
    redisTemplate.delete(key);
  }

  @Override
  public Optional<String> loadUserIdByRefreshToken(String refreshToken) {
    String key = "RT:" + refreshToken;
    String value = (String) redisTemplate.opsForValue().get(key);
    if (value == null) {
      return Optional.empty();
    }
    return value.split(":").length > 1 ? Optional.of(value.split(":")[0]) : Optional.empty();
  }
}
