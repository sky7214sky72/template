package com.core.auth.application.service;

import com.core.auth.application.port.in.ReissueTokenUseCase;
import com.core.auth.application.port.in.result.AuthToken;
import com.core.auth.application.port.out.DeleteRefreshTokenPort;
import com.core.auth.application.port.out.GenerateTokenPort;
import com.core.auth.application.port.out.LoadRefreshTokenPort;
import com.core.auth.application.port.out.LoadUserPort;
import com.core.auth.application.port.out.SaveRefreshTokenPort;
import com.core.auth.domain.User;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {

  private final GenerateTokenPort generateTokenPort;
  private final LoadUserPort loadUserPort;
  private final LoadRefreshTokenPort loadRefreshTokenPort;
  private final SaveRefreshTokenPort saveRefreshTokenPort;
  private final DeleteRefreshTokenPort deleteRefreshTokenPort;

  private final long refreshTokenTtl;

  @Override
  public AuthToken refresh(String refreshToken) {
    String userId = loadRefreshTokenPort.loadUserIdByRefreshToken(refreshToken)
        .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
    User user = loadUserPort.loadUserByUserId(UUID.fromString(userId))
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    AuthToken authToken = generateTokenPort.generateToken(user);
    deleteRefreshTokenPort.deleteRefreshToken(refreshToken);
    saveRefreshTokenPort.saveRefreshToken(authToken.tokenId(), authToken.refreshToken(),
        user.getId().toString(), this.refreshTokenTtl);
    return authToken;
  }
}
