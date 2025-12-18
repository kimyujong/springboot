package com.likeyou.safety.domain.dat.fall.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FallEventDto {

    private final String eventId;      // 예: "FALL-001"
    private final String cctvName;     // 예: "광안리 CCTV-3"
    private final String location;     // 예: "광안리 해변 27-10"
    private final String time;         // 예: "2025-12-09 12:22"
    private final String status;       // DETECTED / RESOLVED
}
