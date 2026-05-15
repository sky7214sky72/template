package com.core.auth.application.service;

import com.core.auth.application.port.in.VerifyTokenUseCase;
import com.core.auth.application.port.in.result.AuthPayload;
import com.core.auth.application.port.out.ParseTokenPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VerifyTokenService implements VerifyTokenUseCase {

  private final ParseTokenPort parseTokenPort;

  @Override
  public AuthPayload verify(String accessToken) {
    return parseTokenPort.parseToken(accessToken);
  }
}
