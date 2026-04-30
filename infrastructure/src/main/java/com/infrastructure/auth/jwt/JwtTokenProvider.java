package com.infrastructure.auth.jwt;

import com.core.auth.domain.User;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  public String createAccessToken(User user) {
    return null;
  }
}
