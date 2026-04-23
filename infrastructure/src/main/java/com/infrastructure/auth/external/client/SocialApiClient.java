package com.infrastructure.auth.external.client;

import com.core.auth.application.port.out.dto.SocialUserProfile;
import com.core.auth.domain.SocialProvider;

public interface SocialApiClient {

  boolean support(SocialProvider socialProvider);

  SocialUserProfile fetch(String authCode);
}
