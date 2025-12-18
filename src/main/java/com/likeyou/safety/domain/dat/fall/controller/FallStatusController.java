package com.likeyou.safety.domain.dat.fall.controller;

import com.likeyou.safety.domain.dat.fall.dto.FallSummaryResponse;
import com.likeyou.safety.domain.dat.fall.service.FallStatusService;
import com.likeyou.safety.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/dat/fall")
@RequiredArgsConstructor
@Tag(name = "Fall", description = "M4 낙상 감지 API")
public class FallStatusController {

    private final FallStatusService fallStatusService;

    /**
     * 낙상 이벤트 summary + 최근 이벤트 리스트
     * GET /admin/dat/fall/summary
     */
    @GetMapping("/summary")
    @Operation(summary = "낙상 이벤트 요약", description = "M4에서 감지된 낙상 이벤트 요약 조회")
    public ApiResponse<FallSummaryResponse> getFallSummary() {
        FallSummaryResponse data = fallStatusService.getFallSummary();
        return ApiResponse.success(data);
    }

    /**
     * M4 분석 시작
     * POST /admin/dat/fall/start?cctvNo=CCTV-01
     */
    @PostMapping("/start")
    @Operation(summary = "M4 분석 시작", description = "특정 CCTV의 낙상 감지를 시작합니다")
    public ApiResponse<Map<String, Object>> startAnalysis(
            @Parameter(description = "CCTV 번호", example = "CCTV-01")
            @RequestParam String cctvNo,
            @Parameter(description = "영상 경로 (선택)")
            @RequestParam(required = false) String videoPath
    ) {
        Map<String, Object> result = fallStatusService.startAnalysis(cctvNo, videoPath);
        return ApiResponse.success(result);
    }

    /**
     * M4 분석 중지
     * POST /admin/dat/fall/stop?cctvNo=CCTV-01
     */
    @PostMapping("/stop")
    @Operation(summary = "M4 분석 중지", description = "특정 CCTV의 낙상 감지를 중지합니다")
    public ApiResponse<Map<String, Object>> stopAnalysis(
            @Parameter(description = "CCTV 번호", example = "CCTV-01")
            @RequestParam String cctvNo
    ) {
        Map<String, Object> result = fallStatusService.stopAnalysis(cctvNo);
        return ApiResponse.success(result);
    }

    /**
     * M4 서버 헬스체크
     * GET /admin/dat/fall/health
     */
    @GetMapping("/health")
    @Operation(summary = "M4 서버 헬스체크")
    public ApiResponse<Map<String, Object>> checkHealth() {
        boolean isHealthy = fallStatusService.checkHealth();
        return ApiResponse.success(Map.of(
                "service", "M4",
                "status", isHealthy ? "healthy" : "unhealthy"
        ));
    }
}