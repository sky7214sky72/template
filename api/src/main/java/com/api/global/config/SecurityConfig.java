package com.api.global.config;

import com.api.auth.filter.JwtAuthenticationFilter;
import com.api.auth.handler.JwtAuthenticationEntryPoint;
import com.core.auth.application.port.in.VerifyTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final VerifyTokenUseCase verifyTokenUseCase;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) {
    http
        // 1. CORS 설정 활성화 (아래 corsConfigurationSource 빈 사용)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))

        // 2. CSRF 비활성화 (REST API는 세션을 쓰지 않으므로 꺼야 함)
        .csrf(AbstractHttpConfigurer::disable)

        // 3. 폼 로그인 & 베이직 HTTP 인증 비활성화
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        // 4. 세션을 쓰지 않고 JWT를 사용할 것이므로 Stateless 설정
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 5. 엔드포인트 권한 설정
        .authorizeHttpRequests(auth -> auth
            // 스웨거 관련 경로는 모두 통과
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/docs", "/api-docs/**",
                "/api/v1/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(new JwtAuthenticationFilter(verifyTokenUseCase),
            UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(handler ->
            handler.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );
    return http.build();
  }

  // 💡 실질적인 CORS 설정부
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // 프론트엔드 로컬 테스트(React, Vue 등)를 위해 모든 오리진 허용
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization")); // JWT 토큰을 프론트가 읽을 수 있게 허용
    config.setAllowCredentials(true); // 쿠키나 인증 정보를 포함한 요청 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}