package com.core.auth.application.port.out.dto;

import com.core.auth.domain.SocialProvider;

public record SocialUserProfile(SocialProvider socialProvider, String providerId, String email) {

}
