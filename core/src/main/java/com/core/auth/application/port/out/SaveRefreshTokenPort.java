package com.core.auth.application.port.out;

public interface SaveRefreshTokenPort {

  void saveRefreshToken(String refreshToken, String userId, long expired);
}
