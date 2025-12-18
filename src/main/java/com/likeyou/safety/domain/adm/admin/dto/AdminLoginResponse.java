package com.likeyou.safety.domain.adm.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginResponse {
    private final Long adminId;
    private final String email;
    private final String role;       // SYSTEM / ADMIN / GOV
    private final String token;      // JWT 토큰
}
