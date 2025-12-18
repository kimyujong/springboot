package com.likeyou.safety.domain.dat.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequest {
    
    @JsonProperty("region_code")
    private Integer regionCode;
    
    @JsonProperty("target_date")
    private String targetDate;  // YYYYMMDD
    
    private String scenario;    // realtime, sunny, rainy, cloudy
}