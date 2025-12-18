package com.likeyou.safety.domain.com.poi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PoiResponse {

    private String poiId;      // UUID → String
    private String type;       // TOILET, EMERGENCY, INFO_BOOTH 등
    private String name;       // 표시용 이름
    private double latitude;
    private double longitude;
}
