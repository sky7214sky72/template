package com.api.auth.handler;

import com.api.global.response.ApiResponse;
import com.core.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull AuthenticationException authException) throws IOException, ServletException {
    // 1. 필터에서 넘겨준 에러 코드가 있는지 확인, 없으면 기본 UNAUTHORIZED
    ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");
    if (errorCode == null) {
      errorCode = ErrorCode.UNAUTHORIZED;
    }

    // 2. HTTP 응답 세팅
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(errorCode.getStatus()); // ErrorCode에 정의된 상태 코드(401 등) 사용

    // 3. 기존 ApiResponse/BusinessException 포맷에 맞춰 JSON
    String jsonResponse = objectMapper.writeValueAsString(
        ApiResponse.fail(errorCode.getCode(), errorCode.getMessage())
    );

    response.getWriter().write(jsonResponse);
  }
}
