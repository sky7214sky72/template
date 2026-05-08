package com.core.auth.application.port.in;

import com.core.auth.application.port.in.result.AuthToken;

public interface ReissueTokenUseCase {

  AuthToken refresh(String refreshToken);
}
