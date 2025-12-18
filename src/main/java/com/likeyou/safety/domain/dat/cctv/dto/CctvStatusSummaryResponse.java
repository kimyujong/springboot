package com.likeyou.safety.domain.dat.cctv.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CctvStatusSummaryResponse {
    private final int activeCount;      // 정상 작동
    private final int inactiveCount;    // 장애 상태
    private final int maintenanceCount; // 유지보수 필요 (status = "MAINT")
}
