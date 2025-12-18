package com.likeyou.safety.domain.dat.prediction.controller;

import com.likeyou.safety.domain.dat.prediction.dto.*;
import com.likeyou.safety.domain.dat.prediction.service.PredictionService;
import com.likeyou.safety.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/dat/prediction")
@RequiredArgsConstructor
@Tag(name = "Prediction", description = "M5 인구 예측 API")
public class PredictionController {

    private final PredictionService predictionService;

    @PostMapping
    @Operation(summary = "인구 예측", description = "특정 지역/날짜의 시간대별 방문자 수 예측")
    public ApiResponse<PredictionResponse> predict(@RequestBody PredictionRequest request) {
        PredictionResponse response = predictionService.predict(request);
        return ApiResponse.success(response);
    }

    @GetMapping("/weather")
    @Operation(summary = "현재 날씨 조회", description = "대시보드용 현재 날씨 정보")
    public ApiResponse<CurrentWeatherResponse> getCurrentWeather(
            @Parameter(description = "지역 코드", example = "26500800")
            @RequestParam(defaultValue = "26500800") Integer regionCode
    ) {
        CurrentWeatherResponse response = predictionService.getCurrentWeather(regionCode);
        return ApiResponse.success(response);
    }

    @GetMapping("/health")
    @Operation(summary = "M5 서버 헬스체크")
    public ApiResponse<Map<String, Object>> checkHealth() {
        boolean isHealthy = predictionService.checkHealth();
        return ApiResponse.success(Map.of(
                "service", "M5",
                "status", isHealthy ? "healthy" : "unhealthy"
        ));
    }
}