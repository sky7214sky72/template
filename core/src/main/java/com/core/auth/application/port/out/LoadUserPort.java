package com.core.auth.application.port.out;

import com.core.auth.domain.User;
import java.util.Optional;
import java.util.UUID;

public interface LoadUserPort {

  Optional<User> loadUserByEmail(String email);

  Optional<User> loadUserByUserId(UUID userId);
}
