package com.likeyou.safety.domain.dat.risk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskResponse {

    private Integer hour;
    private Integer count;
    private List<RoadRiskItem> data;
}