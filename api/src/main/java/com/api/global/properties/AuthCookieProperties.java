package com.api.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.cookie")
public record AuthCookieProperties(
    String name,
    boolean secure,
    String sameSite,
    String domain) {

}
