package com.core.auth.application.port.out;

import com.core.auth.domain.User;

public interface SaveUserPort {

  void save(User user);
}
