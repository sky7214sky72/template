package com.infrastructure.global.config.jpa;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
// infrastructure 모듈 내의 JpaRepository 위치 명시
@EnableJpaRepositories(basePackages = "com.infrastructure.auth.persistence.repository")
// infrastructure 모듈 내의 JPA Entity 위치 명시
@EntityScan(basePackages = "com.infrastructure.auth.persistence.entity")
public class JpaConfig {
  // JPA 관련 추가 설정이 필요하다면 이곳에 작성합니다.
}
