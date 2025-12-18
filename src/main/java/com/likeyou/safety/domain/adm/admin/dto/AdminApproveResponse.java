package com.likeyou.safety.domain.adm.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminApproveResponse {

    private final Long adminId;
    private final String email;
    private final String status;
    private final Long approverId;
}
