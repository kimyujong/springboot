package com.likeyou.safety.domain.dat.crowd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 혼잡도 관련 경보 / 로그 카드용 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrowdAlertResponse {

    private Long id;          // 로그 ID
    private String type;      // warning / danger 등
    private String message;   // "서면번화가 혼잡도 주의 단계"
    private String time;      // "10분 전", "2025-12-09 10:20"
    private String location;  // "서면역 7번 출구"
    private String status;    // active / completed
}
