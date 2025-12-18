package com.likeyou.safety.domain.dat.crowd.controller;

import com.likeyou.safety.domain.dat.crowd.dto.CrowdSummaryResponse;
import com.likeyou.safety.domain.dat.crowd.service.CrowdService;
import com.likeyou.safety.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/dat")
@RequiredArgsConstructor
@Tag(name = "Crowd", description = "M3 혼잡도 분석 API")
public class CrowdController {

    private final CrowdService crowdService;

    /**
     * 혼잡도 요약 조회
     * GET /admin/dat/crowd
     */
    @GetMapping("/crowd")
    @Operation(summary = "혼잡도 요약 조회", description = "M3에서 분석된 혼잡도 로그를 조회합니다")
    public ApiResponse<CrowdSummaryResponse> getCrowdSummary() {
        CrowdSummaryResponse data = crowdService.getCrowdSummary();  // ✅ 수정됨
        return ApiResponse.success(data);
    }

    /**
     * M3 분석 시작
     * POST /admin/dat/crowd/start?cctvNo=CCTV-01
     */
    @PostMapping("/crowd/start")
    @Operation(summary = "M3 분석 시작", description = "특정 CCTV의 혼잡도 분석을 시작합니다")
    public ApiResponse<Map<String, Object>> startAnalysis(
            @Parameter(description = "CCTV 번호", example = "CCTV-01")
            @RequestParam String cctvNo,
            @Parameter(description = "영상 경로 (선택)")
            @RequestParam(required = false) String videoPath
    ) {
        Map<String, Object> result = crowdService.startAnalysis(cctvNo, videoPath);
        return ApiResponse.success(result);
    }

    /**
     * M3 분석 중지
     * POST /admin/dat/crowd/stop?cctvNo=CCTV-01
     */
    @PostMapping("/crowd/stop")
    @Operation(summary = "M3 분석 중지", description = "특정 CCTV의 혼잡도 분석을 중지합니다")
    public ApiResponse<Map<String, Object>> stopAnalysis(
            @Parameter(description = "CCTV 번호", example = "CCTV-01")
            @RequestParam String cctvNo
    ) {
        Map<String, Object> result = crowdService.stopAnalysis(cctvNo);
        return ApiResponse.success(result);
    }

    /**
     * M3 서버 헬스체크
     * GET /admin/dat/crowd/health
     */
    @GetMapping("/crowd/health")
    @Operation(summary = "M3 서버 헬스체크")
    public ApiResponse<Map<String, Object>> checkHealth() {
        boolean isHealthy = crowdService.checkHealth();
        return ApiResponse.success(Map.of(
                "service", "M3",
                "status", isHealthy ? "healthy" : "unhealthy"
        ));
    }
}