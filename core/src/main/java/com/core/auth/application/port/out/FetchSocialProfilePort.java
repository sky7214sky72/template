package com.core.auth.application.port.out;

import com.core.auth.application.port.in.command.SocialLoginCommand;
import com.core.auth.application.port.out.dto.SocialUserProfile;

public interface FetchSocialProfilePort {

  SocialUserProfile fetchProfile(SocialLoginCommand command);
}
