package com.likeyou.safety.domain.adm.admin.entity;

public enum AdminStatus {
    PENDING,   // 가입 신청 상태 (승인 전)
    ACTIVE,     // 승인 완료, 로그인 가능
    REJECT,    // 가입 신청 거절 상태
    HOLD,       // 관리자 권한 보류 상태
    REVOKED     // 관리자 권한 취소 상태
}
