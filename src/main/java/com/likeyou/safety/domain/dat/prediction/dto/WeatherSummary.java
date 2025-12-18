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
public class WeatherSummary {
    private String condition;
    
    @JsonProperty("avg_temp")
    private Double avgTemp;
    
    @JsonProperty("total_rain")
    private Double totalRain;
    
    private String source;
}