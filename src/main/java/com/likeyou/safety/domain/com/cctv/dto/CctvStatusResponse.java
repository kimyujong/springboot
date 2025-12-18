package com.likeyou.safety.domain.com.cctv.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CctvStatusResponse {

    private final String address;       // 대표 이름
    private final String cctvIdx;       // CCTV_01 형식
    private final String status;        // ACTIVE / INACTIVE
    private final String updatedAt;     // String으로 반환
}