package com.infrastructure.auth.persistence;

import com.core.auth.application.port.out.SaveSocialAccountPort;
import com.core.auth.domain.User;
import com.infrastructure.auth.persistence.entity.SocialAccountEntity;
import com.infrastructure.auth.persistence.entity.UserEntity;
import com.infrastructure.auth.persistence.repository.SocialAccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAccountPersistenceAdapter implements SaveSocialAccountPort {

  private final SocialAccountJpaRepository socialAccountJpaRepository;

  @Override
  public void save(User user) {
    UserEntity userEntity = UserEntity.fromDomain(user);
    user.getSocialAccounts().forEach(socialAccount -> {
      if (!socialAccountJpaRepository.existsByUserAndSocialProvider(userEntity, socialAccount.getSocialProvider())) {
        socialAccountJpaRepository.save(SocialAccountEntity.fromDomain(socialAccount, userEntity));
      }
    });
  }
}
