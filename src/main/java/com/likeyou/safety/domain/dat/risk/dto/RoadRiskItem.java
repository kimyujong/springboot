package com.likeyou.safety.domain.dat.risk.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadRiskItem {

    @JsonProperty("unique_road_id")
    private Integer uniqueRoadId;

    private String name;

    @JsonProperty("risk_score")
    private Double riskScore;

    @JsonProperty("risk_level")
    private String riskLevel;

    // GeoJSON Geometry: { "type": "LineString", "coordinates": [[x,y], ...] }
    private Map<String, Object> geometry;
}