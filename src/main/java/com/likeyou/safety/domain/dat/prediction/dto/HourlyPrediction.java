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
public class HourlyPrediction {
    private Integer hour;
    private Integer count;
    
    @JsonProperty("capacity_limit")
    private Integer capacityLimit;
    
    @JsonProperty("is_over_capacity")
    private Boolean isOverCapacity;
}