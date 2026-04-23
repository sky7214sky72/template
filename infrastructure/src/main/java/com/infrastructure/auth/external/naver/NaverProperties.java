package com.infrastructure.auth.external.naver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.naver")
public record NaverProperties(
    String tokenUri,
    String meUri,
    String redirectUri,
    String clientId,
    String clientSecret
) { }
