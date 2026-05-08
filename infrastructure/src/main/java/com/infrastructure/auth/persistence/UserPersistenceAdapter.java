package com.infrastructure.auth.persistence;

import com.core.auth.application.port.out.LoadUserPort;
import com.core.auth.application.port.out.SaveUserPort;
import com.core.auth.domain.User;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import com.infrastructure.auth.persistence.entity.SocialAccountEntity;
import com.infrastructure.auth.persistence.entity.UserEntity;
import com.infrastructure.auth.persistence.repository.SocialAccountJpaRepository;
import com.infrastructure.auth.persistence.repository.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

  private final UserJpaRepository userJpaRepository;
  private final SocialAccountJpaRepository socialAccountJpaRepository;

  @Transactional(readOnly = true)
  @Override
  public Optional<User> loadUserByEmail(String email) {
    return userJpaRepository.findByEmail(email).map(UserEntity::toDomain);
  }

  @Transactional
  @Override
  public User save(User user) {
    if (user.getId() == null) {
      //신규 가입
      UserEntity saved = userJpaRepository.save(UserEntity.fromDomain(user));
      return UserEntity.toDomain(saved);
    } else {
      // 기존 유저는 업데이트
      UserEntity userEntity = userJpaRepository.findById(user.getId())
          .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
      userEntity.updateProfile(user);
      return UserEntity.toDomain(userEntity);
    }
  }
}
