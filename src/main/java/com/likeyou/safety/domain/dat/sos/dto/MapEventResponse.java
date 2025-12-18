package com.likeyou.safety.domain.dat.sos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapEventResponse {
    private String id;
    private String type;        // "fall" | "sos"
    private String severity;    // "emergency" | "warning"
    private Double latitude;
    private Double longitude;
    private String location;    // 주소 또는 CCTV 이름
    private String message;
    private String status;      // "new" | "dispatched" | "resolved"
    private String time;        // 발생 시간
    private boolean isCctv;     // CCTV 감지 여부
}