package com.core.auth.application.port.in;

import com.core.auth.application.port.in.result.AuthPayload;

public interface VerifyTokenUseCase {

  AuthPayload verify(String accessToken);
}
