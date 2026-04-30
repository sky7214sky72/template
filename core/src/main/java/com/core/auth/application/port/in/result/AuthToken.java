package com.core.auth.application.port.in.result;

public record AuthToken(
    String accessToken,
    String refreshToken,
    String tokenId) {

}
