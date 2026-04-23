package com.core.auth.application.port.out.dto;

import com.core.auth.domain.SocialProvider;
import lombok.Builder;

@Builder
public record SocialUserProfile(SocialProvider socialProvider, String providerId, String email) {

  public static SocialUserProfile create(SocialProvider socialProvider, String providerId, String email) {
    return SocialUserProfile.builder()
        .socialProvider(socialProvider)
        .providerId(providerId)
        .email(email)
        .build();
  }
}
