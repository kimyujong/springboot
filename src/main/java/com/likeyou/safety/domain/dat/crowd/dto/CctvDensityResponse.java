package com.likeyou.safety.domain.dat.crowd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 실시간 CCTV 밀집도 정보 (지도 / CCTV 모니터링용)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CctvDensityResponse {

    private String cctvIdx;   // ✅ cctvId → cctvIdx
    private String address;   // ✅ location → address  
    private String status;    // active / warning / inactive
    private Integer density;  // ✅ 0 ~ 100 (정수)
    private String updatedAt; // ✅ timestamp → updatedAt
}