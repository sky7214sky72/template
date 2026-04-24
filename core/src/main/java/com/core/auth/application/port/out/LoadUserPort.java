package com.core.auth.application.port.out;

import com.core.auth.domain.User;
import java.util.Optional;

public interface LoadUserPort {

  Optional<User> loadUserByEmail(String email);
}
