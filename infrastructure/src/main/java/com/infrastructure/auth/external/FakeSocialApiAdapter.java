package com.infrastructure.auth.external;

import static com.core.common.exception.ErrorCode.UNAUTHORIZED;

import com.core.auth.application.port.in.command.SocialLoginCommand;
import com.core.auth.application.port.out.FetchSocialProfilePort;
import com.core.auth.application.port.out.dto.SocialUserProfile;
import com.core.common.exception.BusinessException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class FakeSocialApiAdapter implements FetchSocialProfilePort {

  private static final String FAKE_AUTH_CODE = "test-code";

  @Override
  public SocialUserProfile fetchProfile(SocialLoginCommand command) {
    if (FAKE_AUTH_CODE.equals(command.authCode())) {
      return SocialUserProfile.create(command.socialProvider(), "test-provider-id",
          "test@example.com");
    }
    throw new BusinessException(UNAUTHORIZED);
  }
}
