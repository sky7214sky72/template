package com.infrastructure.global.config.usecase;

import com.core.auth.application.port.in.SocialLoginUseCase;
import com.core.auth.application.port.out.FetchSocialProfilePort;
import com.core.auth.application.port.out.GenerateTokenPort;
import com.core.auth.application.port.out.LoadUserPort;
import com.core.auth.application.port.out.SaveRefreshTokenPort;
import com.core.auth.application.port.out.SaveSocialAccountPort;
import com.core.auth.application.port.out.SaveUserPort;
import com.core.auth.application.service.SocialLoginService;
import com.infrastructure.global.config.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@RequiredArgsConstructor
public class AuthUseCaseConfig {

  private final RedisProperties redisProperties;

  @Bean
  public SocialLoginUseCase socialLoginUseCase(FetchSocialProfilePort fetchSocialProfilePort,
      LoadUserPort loadUserPort,
      SaveUserPort saveUserPort,
      GenerateTokenPort generateTokenPort,
      SaveRefreshTokenPort saveRefreshTokenPort,
      SaveSocialAccountPort  saveSocialAccountPort) {
    return new SocialLoginService(
        fetchSocialProfilePort,
        loadUserPort,
        saveUserPort,
        generateTokenPort,
        saveRefreshTokenPort,
        saveSocialAccountPort,
        redisProperties.refreshTtl()
    );
  }
}
