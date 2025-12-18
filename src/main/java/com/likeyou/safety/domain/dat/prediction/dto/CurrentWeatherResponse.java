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
public class CurrentWeatherResponse {
    private Double temp;
    
    @JsonProperty("condition_text")
    private String conditionText;
    
    @JsonProperty("condition_code")
    private Integer conditionCode;
    
    @JsonProperty("rain_amount")
    private Double rainAmount;
    
    private Double humidity;
    
    @JsonProperty("base_time")
    private String baseTime;
}