package com.infrastructure.auth.jwt;

import com.core.auth.application.port.in.result.AuthToken;
import com.core.auth.application.port.out.GenerateTokenPort;
import com.core.auth.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements GenerateTokenPort {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public AuthToken generateToken(User user) {
    //고유 식별자
    String tokenId = UUID.randomUUID().toString();
    UUID refreshToken = UUID.randomUUID();
    String accessToken = jwtTokenProvider.createAccessToken(user);
    return new AuthToken(tokenId, accessToken, refreshToken.toString());
  }
}
