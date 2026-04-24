package com.infrastructure.auth.persistence.entity;

import com.core.auth.domain.SocialAccount;
import com.core.auth.domain.SocialProvider;
import com.infrastructure.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "public", name = "social_account", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"social_provider", "provider_id"})})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SocialAccountEntity extends BaseTimeEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SocialProvider socialProvider;
  @Column(nullable = false)
  private String providerId;

  public SocialAccount toDomain(SocialAccountEntity socialAccountEntity) {
    return SocialAccount.builder()
        .socialProvider(socialAccountEntity.getSocialProvider())
        .providerId(socialAccountEntity.getProviderId())
        .build();
  }

  public static SocialAccountEntity fromDomain(SocialAccount socialAccount, UserEntity userEntity) {
    return SocialAccountEntity.builder()
        .socialProvider(socialAccount.getSocialProvider())
        .providerId(socialAccount.getProviderId())
        .user(userEntity)
        .build();
  }
}
