package com.likeyou.safety.domain.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Boolean success;
    private List<LatLng> path;
    private RouteInfo info;
    private String error;
}