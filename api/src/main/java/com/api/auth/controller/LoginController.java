package com.api.auth.controller;

import com.api.auth.dto.SocialLoginRequest;
import com.api.auth.dto.SocialLoginResponse;
import com.api.auth.support.CookieProvider;
import com.api.global.response.ApiResponse;
import com.core.auth.application.port.in.LogoutUseCase;
import com.core.auth.application.port.in.ReissueTokenUseCase;
import com.core.auth.application.port.in.SocialLoginUseCase;
import com.core.auth.application.port.in.result.AuthToken;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
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

  private final SocialLoginUseCase socialLoginUseCase;
  private final ReissueTokenUseCase reissueTokenUseCase;
  private final LogoutUseCase logoutUseCase;
  private final CookieProvider cookieProvider;

  //로그인
  @PostMapping("/login")
  public ApiResponse<SocialLoginResponse> login(@Valid @RequestBody SocialLoginRequest request,
      HttpServletResponse response) {
    AuthToken authToken = socialLoginUseCase.login(request.toCommand());
    //응답 헤더에 쿠키 주입
    cookieProvider.responseAddCookie(response, authToken.refreshToken());
    return ApiResponse.success(new SocialLoginResponse(authToken.accessToken()));
  }

  //토큰 갱신
  @PostMapping("/refresh")
  public ApiResponse<SocialLoginResponse> refresh(
      @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken == null) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    AuthToken authToken = reissueTokenUseCase.refresh(refreshToken);
    //응답 헤더에 쿠키 주입
    cookieProvider.responseAddCookie(response, authToken.refreshToken());
    return ApiResponse.success(new SocialLoginResponse(authToken.accessToken()));
  }

  //로그아웃
  @PostMapping("/logout")
  public ApiResponse<Void> logout(
      @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken == null) {
      throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
    logoutUseCase.logout(refreshToken);
    //응답 헤더에 쿠키 주입
    cookieProvider.responseAddCookie(response, null);
    return ApiResponse.success();
  }
}
