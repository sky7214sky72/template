package com.api.auth.controller;

import com.api.auth.dto.SocialLoginRequest;
import com.api.auth.dto.SocialLoginResponse;
import com.api.global.response.ApiResponse;
import com.core.auth.application.port.in.SocialLoginUseCase;
import com.core.auth.application.port.in.result.AuthToken;
import io.swagger.v3.oas.annotations.tags.Tag;
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

  private final SocialLoginUseCase socialLoginUseCase;

  //로그인
  @PostMapping("/login")
  public ApiResponse<SocialLoginResponse> login(@Valid @RequestBody SocialLoginRequest request, HttpServletResponse response) {
    AuthToken authToken = socialLoginUseCase.login(request.toCommand());
    // 리프레시 토큰을 시큐어 쿠키로 변환 (웹 어댑터의 책임)
    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authToken.refreshToken())
        .httpOnly(true)
        .secure(true) // 운영 환경(HTTPS)에서는 true, 로컬 HTTP 테스트 시에는 false 처리 필요
        .sameSite("Strict") // CSRF 방어
        .path("/")
        .maxAge(14L * 24L * 60L * 60L) // 예: 14일 (초 단위)
        .build();
    //응답 헤더에 쿠키 주입
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    return ApiResponse.success(new SocialLoginResponse(authToken.accessToken()));
  }
}
