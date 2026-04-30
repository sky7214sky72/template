package com.core.auth.application.service;

import com.core.auth.application.port.in.SocialLoginUseCase;
import com.core.auth.application.port.in.command.SocialLoginCommand;
import com.core.auth.application.port.in.result.AuthToken;
import com.core.auth.application.port.out.FetchSocialProfilePort;
import com.core.auth.application.port.out.GenerateTokenPort;
import com.core.auth.application.port.out.LoadUserPort;
import com.core.auth.application.port.out.SaveRefreshTokenPort;
import com.core.auth.application.port.out.SaveSocialAccountPort;
import com.core.auth.application.port.out.SaveUserPort;
import com.core.auth.application.port.out.dto.SocialUserProfile;
import com.core.auth.domain.SocialAccount;
import com.core.auth.domain.User;
import com.core.auth.domain.UserRole;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class SocialLoginService implements SocialLoginUseCase {

  private final FetchSocialProfilePort fetchSocialProfilePort;
  private final LoadUserPort loadUserPort;
  private final SaveUserPort saveUserPort;
  private final GenerateTokenPort generateTokenPort;
  private final SaveRefreshTokenPort saveRefreshTokenPort;
  private final SaveSocialAccountPort saveSocialAccountPort;

  private final long refreshTokenTtl;

  @Override
  public AuthToken login(SocialLoginCommand command) {
    // 1. 소셜 프로필 가져오기
    SocialUserProfile socialUserProfile = fetchSocialProfilePort.fetchProfile(command);
    // 2. 이메일, SocialProvider 기반으로 유저 조회
    User user = loadUserPort.loadUserByEmail(socialUserProfile.email())
        .orElseGet(() ->
            //유저가 없다면 User 객체 생성
            saveUserPort.save(User.builder()
                .email(socialUserProfile.email())
                .role(UserRole.USER)
                .socialAccounts(List.of(SocialAccount.builder()
                    .socialProvider(socialUserProfile.socialProvider())
                    .providerId(socialUserProfile.providerId())
                    .build()))
                .build())
        );

    // 소셜 계정 정보 저장
    saveSocialAccountPort.save(user);

    //리프레시 토큰, 액세스 토큰 생성
    AuthToken authToken = generateTokenPort.generateToken(user);

    //리프레시 토큰 저장
    saveRefreshTokenPort.saveRefreshToken(authToken.refreshToken(), user.getId().toString(),
        this.refreshTokenTtl);

    return authToken;
  }
}
