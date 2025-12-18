package com.likeyou.safety.domain.dat.sos.controller;

import com.likeyou.safety.domain.dat.sos.dto.*;
import com.likeyou.safety.domain.dat.sos.service.SosStatusService;
import com.likeyou.safety.domain.dat.sos.service.SosMapService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dat/sos")
public class SosStatusController {

    private final SosStatusService sosStatusService;
    private final SosMapService sosMapService;  // ✅ 추가

    /**
     * SOS 요약 정보 조회
     */
    @GetMapping("/summary")
    public ApiResponse<SosListResponse> getSosSummary() {
        return ApiResponse.success(sosStatusService.getSosStatus());
    }

    /**
     * SOS 이벤트 목록 조회 (프론트엔드용)
     */
    @GetMapping
    public ApiResponse<List<SosEventResponse>> getEvents(
            @RequestParam(defaultValue = "20") int limit) {
        return ApiResponse.success(sosStatusService.getEvents(limit));
    }

    /**
     * 시간대별 SOS 통계
     */
    @GetMapping("/stats/hourly")
    public ApiResponse<List<HourlyStatResponse>> getHourlyStats() {
        return ApiResponse.success(sosStatusService.getHourlyStats());
    }

    /**
     * 구역별 SOS 통계
     */
    @GetMapping("/stats/zone")
    public ApiResponse<List<ZoneStatResponse>> getZoneStats() {
        return ApiResponse.success(sosStatusService.getZoneStats());
    }

    /**
     * SOS 이벤트 상태 업데이트
     */
    @PutMapping("/{eventId}")
    public ApiResponse<Map<String, Object>> updateEventStatus(
            @PathVariable String eventId,
            @RequestBody SosUpdateRequest request) {
        boolean success = sosStatusService.updateEventStatus(eventId, request);
        return ApiResponse.success(Map.of(
                "success", success,
                "eventId", eventId,
                "newStatus", request.getStatus()
        ));
    }

    // ========== 지도용 API (M4 낙상 + Citizen SOS) ==========

    /**
     * 지도용 이벤트 목록 조회
     * GET /admin/dat/sos/map-events
     */
    @GetMapping("/map-events")
    public ApiResponse<List<MapEventResponse>> getMapEvents() {
        return ApiResponse.success(sosMapService.getMapEvents());
    }

    /**
     * 지도 이벤트 상태 업데이트 (출동 처리)
     * PUT /admin/dat/sos/map-events/{eventId}?eventType=sos&status=dispatched
     */
    @PutMapping("/map-events/{eventId}")
    public ApiResponse<Map<String, Object>> updateMapEventStatus(
            @PathVariable String eventId,
            @RequestParam String eventType,
            @RequestParam String status) {
        boolean success = sosMapService.updateEventStatus(eventId, eventType, status);
        return ApiResponse.success(Map.of(
                "success", success,
                "eventId", eventId,
                "newStatus", status
        ));
    }
}