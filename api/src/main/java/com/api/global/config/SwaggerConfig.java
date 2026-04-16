package com.api.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class SwaggerConfig {

  private static final String JWT_SCHEME_NAME = "JWT_TOKEN";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(apiInfo())
        .servers(apiServers())
        .components(apiComponents())
        .addSecurityItem(apiSecurityRequirement());
  }

  private Info apiInfo() {
    return new Info()
        .title("Template API 명세서")
        .description("사이드 프로젝트를 위한 헥사고날 아키텍처 API 문서입니다.")
        .version("v1.0.0");
  }

  // 서버 환경 드롭다운 설정 (로컬, 개발서버 등)
  private List<Server> apiServers() {
    Server localServer = new Server().url("http://localhost:8080").description("Local Server");
    Server devServer = new Server().url("https://dev-api.template.com").description("Dev Server");
    // 운영 서버는 보안상 넣지 않습니다.
    return List.of(localServer, devServer);
  }

  // JWT 보안 스키마 정의 (우측 상단 자물쇠 아이콘)
  private Components apiComponents() {
    return new Components()
        .addSecuritySchemes(JWT_SCHEME_NAME, new SecurityScheme()
            .name(JWT_SCHEME_NAME)
            .type(SecurityScheme.Type.HTTP) // HTTP 방식
            .scheme("bearer")               // Bearer 토큰
            .bearerFormat("JWT"));
  }

  // 전역 보안 요구사항 (모든 API에 자물쇠가 자동으로 걸리게 함)
  private SecurityRequirement apiSecurityRequirement() {
    return new SecurityRequirement().addList(JWT_SCHEME_NAME);
  }
}
