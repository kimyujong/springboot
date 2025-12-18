package com.likeyou.safety.domain.app.navigation.controller;

import com.likeyou.safety.domain.navigation.dto.*;
import com.likeyou.safety.domain.navigation.service.NavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/navigation")
@RequiredArgsConstructor
@Tag(name = "Citizen Navigation", description = "시민 앱 네비게이션 API")
public class AppNavigationController {

    private final NavigationService navigationService;

    @PostMapping("/route")
    @Operation(summary = "안심 경로 계산")
    public ResponseEntity<RouteResponse> calculateRoute(@RequestBody RouteRequest request) {
        RouteResponse response = navigationService.calculateRoute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/heatmap")
    @Operation(summary = "히트맵 조회")
    public ResponseEntity<HeatmapResponse> getHeatmap() {
        HeatmapResponse response = navigationService.getHeatmap();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cctv")
    @Operation(summary = "CCTV 정보 조회")
    public ResponseEntity<CCTVResponse> getCCTV() {
        CCTVResponse response = navigationService.getCCTV();
        return ResponseEntity.ok(response);
    }
}