package com.likeyou.safety.domain.com.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponse {

    private Long uniqueRoadId;
    private Integer hour;
    private String osmid;
    private String name;
    private String dong;
    private Double riskScore;
}
