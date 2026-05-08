package com.api.auth.controller;

import com.api.auth.dto.SocialLoginRequest;
import com.api.auth.dto.SocialLoginResponse;
import com.api.global.response.ApiResponse;
import com.core.auth.application.port.in.LogoutUseCase;
import com.core.auth.application.port.in.ReissueTokenUseCase;
import com.core.auth.application.port.in.SocialLoginUseCase;
import com.core.auth.application.port.in.result.AuthToken;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import com.infrastructure.global.config.properties.RedisProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {

  private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
  private static final String SAME_SITE_COOKIE_NAME = "sameSite";

  private final RedisProperties redisProperties;
  private final SocialLoginUseCase socialLoginUseCase;
  private final ReissueTokenUseCase reissueTokenUseCase;
  private final LogoutUseCase logoutUseCase;

  //로그인
  @PostMapping("/login")
  public ApiResponse<SocialLoginResponse> login(@Valid @RequestBody SocialLoginRequest request,
      HttpServletResponse response) {
    AuthToken authToken = socialLoginUseCase.login(request.toCommand());
    // 리프레시 토큰을 시큐어 쿠키로 변환 (웹 어댑터의 책임)
    ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME,
            authToken.refreshToken())
        .httpOnly(true)
        .secure(true) // 운영 환경(HTTPS)에서는 true, 로컬 HTTP 테스트 시에는 false 처리 필요
        .sameSite(SAME_SITE_COOKIE_NAME) // CSRF 방어
        .path("/")
        .maxAge(redisProperties.refreshTtl()) // 예: 14일 (초 단위)
        .build();
    //응답 헤더에 쿠키 주입
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    return ApiResponse.success(new SocialLoginResponse(authToken.accessToken()));
  }

  //토큰 갱신
  @PostMapping("/refresh")
  public ApiResponse<SocialLoginResponse> refresh(HttpServletRequest request,
      HttpServletResponse response) {
    Cookie[] cookies = request.getCookies(); // 모든 쿠키 배열로 가져오기
    String refreshToken = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
          refreshToken = cookie.getValue(); // 쿠키 값 가져오기
          break;
        }
      }
    }
    if (refreshToken == null) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    AuthToken authToken = reissueTokenUseCase.refresh(refreshToken);
    // 리프레시 토큰을 시큐어 쿠키로 변환 (웹 어댑터의 책임)
    ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME,
            authToken.refreshToken())
        .httpOnly(true)
        .secure(true) // 운영 환경(HTTPS)에서는 true, 로컬 HTTP 테스트 시에는 false 처리 필요
        .sameSite(SAME_SITE_COOKIE_NAME) // CSRF 방어
        .path("/")
        .maxAge(redisProperties.refreshTtl()) // 예: 14일 (초 단위)
        .build();
    //응답 헤더에 쿠키 주입
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    return ApiResponse.success(new SocialLoginResponse(authToken.accessToken()));
  }

  //로그아웃
  @PostMapping("/logout")
  public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies(); // 모든 쿠키 배열로 가져오기
    String refreshToken = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
          refreshToken = cookie.getValue(); // 쿠키 값 가져오기
          break;
        }
      }
    }
    if (refreshToken == null) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    logoutUseCase.logout(refreshToken);
    // 리프레시 토큰을 시큐어 쿠키로 변환 (웹 어댑터의 책임)
    ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, null)
        .httpOnly(true)
        .secure(true) // 운영 환경(HTTPS)에서는 true, 로컬 HTTP 테스트 시에는 false 처리 필요
        .sameSite(SAME_SITE_COOKIE_NAME) // CSRF 방어
        .path("/")
        .maxAge(0) // 로그아웃이라 0처리
        .build();
    //응답 헤더에 쿠키 주입
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    return ApiResponse.success();
  }
}
