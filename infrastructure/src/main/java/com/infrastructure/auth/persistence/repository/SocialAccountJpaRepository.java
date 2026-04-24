package com.infrastructure.auth.persistence.repository;

import com.core.auth.domain.SocialProvider;
import com.infrastructure.auth.persistence.entity.SocialAccountEntity;
import com.infrastructure.auth.persistence.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAccountJpaRepository extends JpaRepository<SocialAccountEntity, Long> {

  Optional<SocialAccountEntity> findBySocialProviderAndProviderId(SocialProvider socialProvider, String providerId);

  boolean existsByUserAndSocialProvider(UserEntity user, SocialProvider socialProvider);
}
