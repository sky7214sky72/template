package com.infrastructure.global.config.usecase;

import com.core.auth.application.port.in.LogoutUseCase;
import com.core.auth.application.port.in.ReissueTokenUseCase;
import com.core.auth.application.port.in.SocialLoginUseCase;
import com.core.auth.application.port.out.DeleteRefreshTokenPort;
import com.core.auth.application.port.out.FetchSocialProfilePort;
import com.core.auth.application.port.out.GenerateTokenPort;
import com.core.auth.application.port.out.LoadRefreshTokenPort;
import com.core.auth.application.port.out.LoadUserPort;
import com.core.auth.application.port.out.SaveRefreshTokenPort;
import com.core.auth.application.port.out.SaveUserPort;
import com.core.auth.application.service.LogoutService;
import com.core.auth.application.service.ReissueTokenService;
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
      SaveRefreshTokenPort saveRefreshTokenPort) {
    return new SocialLoginService(
        fetchSocialProfilePort,
        loadUserPort,
        saveUserPort,
        generateTokenPort,
        saveRefreshTokenPort,
        redisProperties.refreshTtl()
    );
  }

  @Bean
  public ReissueTokenUseCase reissueTokenUseCase(GenerateTokenPort generateTokenPort,
      LoadUserPort loadUserPort, LoadRefreshTokenPort loadRefreshTokenPort,
      SaveRefreshTokenPort saveRefreshTokenPort, DeleteRefreshTokenPort deleteRefreshTokenPort) {
    return new ReissueTokenService(generateTokenPort, loadUserPort, loadRefreshTokenPort,
        saveRefreshTokenPort, deleteRefreshTokenPort, redisProperties.refreshTtl());
  }

  @Bean
  public LogoutUseCase logoutUseCase(DeleteRefreshTokenPort deleteRefreshTokenPort) {
    return new LogoutService(deleteRefreshTokenPort);
  }
}
