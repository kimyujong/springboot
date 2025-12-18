package com.likeyou.safety.domain.adm.admin.dto;

import lombok.Getter;

@Getter
public class AdminApproveRequest {

    private Long approverId;      // SYSTEM 관리자 ID
    private Long targetAdminId;   // 승인 대상 ID
    private String status;        // ACTIVE / REJECT / HOLD
}
