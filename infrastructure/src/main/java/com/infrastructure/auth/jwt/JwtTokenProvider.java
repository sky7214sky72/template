package com.infrastructure.auth.jwt;

import com.core.auth.domain.User;
import com.infrastructure.auth.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long accessTokenValidityInSeconds;

  public JwtTokenProvider(JwtProperties jwtProperties) {
    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidityInSeconds = jwtProperties.accessTokenValidityInSeconds();
  }

  public String createAccessToken(User user) {
    long now = (new Date()).getTime();
    Date validity = new Date(now + accessTokenValidityInSeconds * 1000);
    return Jwts.builder()
        .subject(String.valueOf(user.getId()))
        .claim("role", user.getRole())
        .issuedAt(new Date(now))
        .expiration(validity)
        .signWith(secretKey)
        .compact();
  }

  public Claims parseClaims(String accessToken) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(accessToken)
        .getPayload();
  }
}
