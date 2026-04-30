package com.core.auth.application.port.out;

import com.core.auth.domain.User;

public interface SaveSocialAccountPort {

  void save(User user);
}
