package com.likeyou.safety.domain.adm.admin.entity;

public enum AdminType {
    SYSTEM,   // 시스템 관리자 (전체 권한)
    ADMIN,    // 지자체/관제센터 관리자
    GOV       // 경찰/소방 등 공공기관 사용자
}