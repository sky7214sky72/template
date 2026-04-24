package com.core.auth.application.port.out;

import com.core.auth.application.port.in.result.AuthToken;
import com.core.auth.domain.User;

public interface GenerateTokenPort {

  AuthToken generateToken(User user);
}
