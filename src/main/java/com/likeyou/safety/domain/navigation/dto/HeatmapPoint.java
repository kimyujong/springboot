package com.likeyou.safety.domain.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapPoint {
    private Double lat;
    private Double lon;
    private Integer density;
}