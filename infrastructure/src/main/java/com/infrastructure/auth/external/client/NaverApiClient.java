package com.infrastructure.auth.external.client;

import com.core.auth.application.port.out.dto.SocialUserProfile;
import com.core.auth.domain.SocialProvider;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import com.infrastructure.auth.external.naver.NaverProfileResponse;
import com.infrastructure.auth.properties.NaverProperties;
import com.infrastructure.auth.external.naver.NaverTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverApiClient implements SocialApiClient {

  private final RestClient restClient;
  private final NaverProperties naverProperties;

  @Override
  public boolean support(SocialProvider socialProvider) {
    return socialProvider == SocialProvider.NAVER;
  }

  @Override
  public SocialUserProfile fetch(String authCode) {
    // 토큰 발급
    NaverTokenResponse tokenResponse = getNaverTokenResponse(authCode);
    // 토큰으로 프로필 조회
    NaverProfileResponse profileResponse = getNaverProfileResponse(
        tokenResponse);

    log.info("[Naver] 성공적으로 프로필을 가져왔습니다. Email: {}", profileResponse.response().email());

    return SocialUserProfile.create(
        SocialProvider.NAVER,
        profileResponse.response().id(),
        profileResponse.response().email()
    );
  }

  private NaverProfileResponse getNaverProfileResponse(NaverTokenResponse tokenResponse) {
    return restClient.get()
        .uri(naverProperties.meUri())
        .header("Authorization", "Bearer " + tokenResponse.accessToken())
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> {
          log.error("[Naver] 프로필 조회 실패. Status: {}", response.getStatusCode());
          throw new BusinessException(ErrorCode.SOCIAL_NOT_FOUND);
        })
        .body(NaverProfileResponse.class);
  }

  private NaverTokenResponse getNaverTokenResponse(String authCode) {
    //1. 파라미터 생성
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("client_id", naverProperties.clientId());
    formData.add("client_secret", naverProperties.clientSecret());
    formData.add("code", authCode);
    formData.add("redirect_uri", naverProperties.redirectUri());
    // 네이버 토큰 발급 API 호출
    return restClient.post()
        .uri(naverProperties.tokenUri())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(formData)
        .retrieve()
        // 4xx, 5xx 에러 발생 시 공통 예외 처리
        .onStatus(HttpStatusCode::isError, (request, response) -> {
          log.error("[Naver] 토큰 발급 실패. Status: {}", response.getStatusCode());
          throw new BusinessException(ErrorCode.SOCIAL_NOT_FOUND);
        })
        .body(NaverTokenResponse.class);
  }
}
