package com.core.auth.application.port.in;

import com.core.auth.application.port.in.command.SocialLoginCommand;
import com.core.auth.application.port.in.result.AuthToken;

public interface SocialLoginUseCase {

  AuthToken login(SocialLoginCommand command);
}
