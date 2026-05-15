package com.infrastructure.auth.jwt;

import com.core.auth.application.port.in.result.AuthPayload;
import com.core.auth.application.port.in.result.AuthToken;
import com.core.auth.application.port.out.GenerateTokenPort;
import com.core.auth.application.port.out.ParseTokenPort;
import com.core.auth.domain.User;
import com.core.auth.domain.UserRole;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements GenerateTokenPort, ParseTokenPort {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public AuthToken generateToken(User user) {
    //고유 식별자
    String tokenId = UUID.randomUUID().toString();
    UUID refreshToken = UUID.randomUUID();
    String accessToken = jwtTokenProvider.createAccessToken(user);
    return new AuthToken(accessToken, refreshToken.toString(), tokenId);
  }

  @Override
  public AuthPayload parseToken(String accessToken) {
    try {
      Claims claims = jwtTokenProvider.parseClaims(accessToken);
      return new AuthPayload(
          UUID.fromString(claims.getSubject()),
          UserRole.valueOf(claims.get("role", String.class))
      );
    } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
      throw new BusinessException(ErrorCode.INVALID_TOKEN);
    } catch (ExpiredJwtException e) {
      throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
    } catch (UnsupportedJwtException e) {
      throw new BusinessException(ErrorCode.UNSUPPORTED_TOKEN);
    }
  }
}
