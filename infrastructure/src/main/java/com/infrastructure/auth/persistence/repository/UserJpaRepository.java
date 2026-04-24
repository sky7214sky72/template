package com.infrastructure.auth.persistence.repository;

import com.core.auth.domain.UserRole;
import com.infrastructure.auth.persistence.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

   Optional<UserEntity> findByEmail(String email);
}
