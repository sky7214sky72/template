package com.api.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * . 로그 저장 시 사용자를 특정 짓기 위한 traceId 발급 필터
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String traceId = UUID.randomUUID().toString().substring(0, 8); // 간단한 traceId 생성 (8자리)
    MDC.put("traceId", traceId); // MDC에 traceId 저장

    try {
      filterChain.doFilter(request, response);
    } finally {
      // 요청 끝나면 MDC를 비워야함 (메모리 누수 방지)
      MDC.clear();
    }
  }
}
