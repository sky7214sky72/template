package com.infrastructure.auth.persistence.entity;

import com.core.auth.domain.User;
import com.core.auth.domain.UserRole;
import com.infrastructure.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "public", name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseTimeEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false, unique = true, length = 100)
  private String email;
  @Column(nullable = false, length = 100)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  public static User toDomain(UserEntity entity) {
    return User.builder()
        .id(entity.getId())
        .email(entity.getEmail())
        .role(entity.getRole())
        .build();
  }

  public static UserEntity fromDomain(User user) {
    return UserEntity.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .build();
  }

  public void updateProfile(User user) {
    this.email = user.getEmail();
    this.role = user.getRole();
  }
}
