package com.core.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SocialAccount {

  private SocialProvider socialProvider;
  private String providerId;
}
