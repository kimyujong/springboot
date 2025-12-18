package com.likeyou.safety.domain.adm.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminListResponse {

    private final Long adminId;
    private final String email;
    private final String name;
    private final String organization;
    private final String position;
    private final String number;
    private final String type;      // SYSTEM / ADMIN / GOV
    private final String status;    // PENDING / ACTIVE / HOLD / REJECT / REVOKED
}
