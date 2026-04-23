package com.infrastructure.auth.external.naver;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverProfileResponse(
    @JsonProperty("resultcode") String resultCode,
    String message,
    NaverResponse response
) {

  // 실제 사용자 정보가 담긴 내부 레코드
  public record NaverResponse(
      String id,
      String email
  ) {

  }
}
