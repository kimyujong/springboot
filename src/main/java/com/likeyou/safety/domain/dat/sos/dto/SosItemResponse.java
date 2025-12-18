package com.likeyou.safety.domain.dat.sos.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SosItemResponse {
    private String sosId;
    private String location;
    private String time;      // "2025-12-09 12:30" 형태
    private String status;    // PENDING / RESOLVED
}
