package com.api.auth.dto;

import com.core.auth.application.port.in.command.SocialLoginCommand;
import com.core.auth.domain.SocialProvider;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;

public record SocialLoginRequest(
    @NotBlank String socialProvider,
    @NotBlank String authCode) {

  public SocialLoginCommand toCommand() {
    try {
      return new SocialLoginCommand(SocialProvider.valueOf(socialProvider.toUpperCase()), authCode);
    } catch (IllegalArgumentException e) {
      throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
    }
  }
}
