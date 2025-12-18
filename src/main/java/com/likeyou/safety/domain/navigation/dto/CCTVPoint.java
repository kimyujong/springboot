package com.likeyou.safety.domain.navigation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CCTVPoint {
    @JsonProperty("cctv_no")
    private Integer cctvNo;
    
    private Double lat;
    private Double lon;
    private Integer density;
}