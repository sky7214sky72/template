package com.core.auth.application.port.out;

import com.core.auth.application.port.in.result.AuthPayload;

public interface ParseTokenPort {

  AuthPayload parseToken(String accessToken);
}
