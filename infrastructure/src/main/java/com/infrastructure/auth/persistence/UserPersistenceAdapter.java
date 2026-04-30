package com.infrastructure.auth.persistence;

import com.core.auth.application.port.out.LoadUserPort;
import com.core.auth.application.port.out.SaveUserPort;
import com.core.auth.domain.User;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import com.infrastructure.auth.persistence.entity.UserEntity;
import com.infrastructure.auth.persistence.repository.UserJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

  private final UserJpaRepository userJpaRepository;

  @Transactional(readOnly = true)
  @Override
  public Optional<User> loadUserByEmail(String email) {
    return userJpaRepository.findByEmail(email).map(UserEntity::toDomain);
  }

  @Transactional
  @Override
  public User save(User user) {
    UserEntity userEntity;
    if (user.getId() == null) {
      //신규 가입
      userEntity = userJpaRepository.save(UserEntity.fromDomain(user));
    } else {
      // 기존 유저는 업데이트
      userEntity = userJpaRepository.findById(user.getId())
          .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
      userEntity.updateProfile(user);
    }
    return UserEntity.toDomain(userEntity);
  }
}
