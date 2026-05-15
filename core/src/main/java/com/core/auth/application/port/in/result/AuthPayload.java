package com.core.auth.application.port.in.result;

import com.core.auth.domain.UserRole;
import java.util.UUID;

public record AuthPayload(
    UUID userId,
    UserRole userRole
) {

}
