package com.core.auth.application.service;

import com.core.auth.application.port.in.LogoutUseCase;
import com.core.auth.application.port.out.DeleteRefreshTokenPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

  private final DeleteRefreshTokenPort deleteRefreshTokenPort;

  @Override
  public void logout(String refreshToken) {
    deleteRefreshTokenPort.deleteRefreshToken(refreshToken);
  }
}
