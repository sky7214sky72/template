package com.core.auth.domain;

import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class User {

  private UUID id;
  private String email;
  private UserRole role;
  @Builder.Default
  private List<SocialAccount> socialAccounts = new ArrayList<>();

  public void linkSocialAccount(SocialAccount socialAccount) {
    // 이미 연동됐는지 확인
    boolean alreadyLinked = socialAccounts.stream()
        .anyMatch(account -> account.getSocialProvider() == socialAccount.getSocialProvider());

    if (alreadyLinked) {
      throw new BusinessException(ErrorCode.DUPLICATE_SOCIAL);
    }

    socialAccounts.add(socialAccount);
  }
}
