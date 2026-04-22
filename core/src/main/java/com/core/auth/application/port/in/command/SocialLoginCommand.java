package com.core.auth.application.port.in.command;

import com.core.auth.domain.SocialProvider;

public record SocialLoginCommand(SocialProvider socialProvider, String token) {

}
