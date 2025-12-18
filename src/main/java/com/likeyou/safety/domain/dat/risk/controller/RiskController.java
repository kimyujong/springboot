package com.likeyou.safety.domain.dat.risk.controller;

import com.likeyou.safety.domain.dat.risk.dto.RiskResponse;
import com.likeyou.safety.domain.dat.risk.service.RiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/dat/risk")
@RequiredArgsConstructor
@Tag(name = "Risk", description = "M1 도로 위험도 API")
public class RiskController {

    private final RiskService riskService;

    @GetMapping
    @Operation(summary = "도로 위험도 조회", description = "특정 시간대의 도로별 위험도를 조회합니다")
    public ResponseEntity<RiskResponse> getRoadRisk(
            @Parameter(description = "조회할 시간대 (0~23)", example = "14")
            @RequestParam(defaultValue = "12") int hour
    ) {
        RiskResponse response = riskService.getRoadRisk(hour);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "M1 서버 헬스체크")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        boolean isHealthy = riskService.checkHealth();
        
        return ResponseEntity.ok(Map.of(
                "service", "M1",
                "status", isHealthy ? "healthy" : "unhealthy"
        ));
    }
}