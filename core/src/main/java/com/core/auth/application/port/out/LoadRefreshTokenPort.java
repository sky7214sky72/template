package com.core.auth.application.port.out;

import java.util.Optional;

public interface LoadRefreshTokenPort {

  Optional<String> loadUserIdByRefreshToken(String refreshToken);
}
