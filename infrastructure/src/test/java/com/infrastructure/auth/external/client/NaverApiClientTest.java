package com.infrastructure.auth.external.client;

import com.core.auth.application.port.out.dto.SocialUserProfile;
import com.infrastructure.global.config.properties.PropertiesConfig;
import com.infrastructure.global.config.restclient.RestClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(classes = {
    NaverApiClient.class,   // 우리가 테스트할 본체
    RestClientConfig.class, // 통신 무전기 (RestClient)
    PropertiesConfig.class  // 네이버 설정값 (NaverProperties)
})
@ActiveProfiles("prod")
class NaverApiClientTest {

  @Autowired
  private NaverApiClient naverApiClient;

  @Test
  @DisplayName("진짜 네이버 서버에 인가 코드를 던져서 토큰을 받아온다")
  void fetchRealNaverToken() {
    // 1단계에서 따온 코드를 여기에 붙여넣으세요 (일회용)
    String realAuthCode = "일회용코드";

    // 실행
    SocialUserProfile profile = naverApiClient.fetch(realAuthCode);

    // 검증
    log.info("성공적으로 가져온 프로필: {}", profile);
    Assertions.assertThat(profile).isNotNull();
  }
}