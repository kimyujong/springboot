package com.likeyou.safety.domain.dat.crowd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * /admin/dat/crowd 응답 전체 래퍼
 *
 * {
 *   "cctvDensity": [...],
 *   "crowdAlerts": [...]
 * }
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrowdSummaryResponse {

    private List<CctvDensityResponse> cctvDensity;
    private List<CrowdAlertResponse> crowdAlerts;
}
