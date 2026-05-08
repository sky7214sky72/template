package com.infrastructure.auth.persistence.entity;

import com.core.auth.domain.User;
import com.core.auth.domain.UserRole;
import com.infrastructure.global.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SocialAccountEntity> socialAccounts = new ArrayList<>();

  public static User toDomain(UserEntity entity) {
    return User.builder()
        .id(entity.getId())
        .email(entity.getEmail())
        .role(entity.getRole())
        .build();
  }

  public static UserEntity fromDomain(User user) {
    UserEntity userEntity = UserEntity.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .socialAccounts(new ArrayList<>())
        .build();
    user.getSocialAccounts().forEach(domainAccount -> userEntity.addSocialAccount(
        SocialAccountEntity.fromDomain(domainAccount)));
    return userEntity;
  }

  public void updateProfile(User user) {
    this.email = user.getEmail();
    this.role = user.getRole();
  }

  public void addSocialAccount(SocialAccountEntity account) {
    this.socialAccounts.add(account);
    account.assignUser(this); // 자식에게 부모 참조 세팅
  }
}
