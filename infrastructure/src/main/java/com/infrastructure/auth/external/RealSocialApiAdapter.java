package com.infrastructure.auth.external;

import com.core.auth.application.port.in.command.SocialLoginCommand;
import com.core.auth.application.port.out.FetchSocialProfilePort;
import com.core.auth.application.port.out.dto.SocialUserProfile;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import com.infrastructure.auth.external.client.SocialApiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!local")
@Component
@RequiredArgsConstructor
public class RealSocialApiAdapter implements FetchSocialProfilePort {

  private final List<SocialApiClient> socialApiClientList;

  @Override
  public SocialUserProfile fetchProfile(SocialLoginCommand command) {
    return socialApiClientList.stream()
        .filter(client -> client.support(command.socialProvider()))
        .findFirst()
        .map(client -> client.fetch(command.authCode()))
        .orElseThrow(() -> new BusinessException(ErrorCode.SOCIAL_NOT_FOUND));
  }
}
