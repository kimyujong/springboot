package com.likeyou.safety.domain.adm.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminSignupResponse {
    private final Long adminId;
    private final String email;
    private final String status;   // PENDING / ACTIVE
}
