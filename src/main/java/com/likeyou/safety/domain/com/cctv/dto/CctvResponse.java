package com.likeyou.safety.domain.com.cctv.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CctvResponse {

    private final String address;     // cctv_addr (대표 이름)
    private final String cctvIdx;     // CCTV_01 형식
    private final double latitude;
    private final double longitude;
    private final String status;      // ACTIVE / INACTIVE
    private final boolean active;     // isActive → active로 변경 (JSON 직렬화 일관성)
}