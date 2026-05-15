package com.api.auth.support;

import com.api.global.properties.AuthCookieProperties;
import com.infrastructure.global.config.properties.RedisProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

  private final AuthCookieProperties authCookieProperties;
  private final RedisProperties redisProperties;

  public void responseAddCookie(HttpServletResponse response, String refreshToken) {
    // 값이 없으면 0(로그아웃), 있으면 properties의 TTL(로그인/갱신)
    long maxAge = (refreshToken == null) ? 0 : redisProperties.refreshTtl();
    // 값이 없으면 빈 문자열("") 세팅 (null 방어)
    String cookieValue = (refreshToken == null) ? "" : refreshToken;
    ResponseCookie cookie = ResponseCookie.from(authCookieProperties.name(), cookieValue)
        .httpOnly(true)
        .secure(authCookieProperties.secure())
        .sameSite(authCookieProperties.sameSite())
        .domain(authCookieProperties.domain()) // 💡 yml에 추가한 도메인 설정도 사용!
        .path("/")
        .maxAge(maxAge)
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
