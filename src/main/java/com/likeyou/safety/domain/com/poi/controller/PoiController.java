package com.likeyou.safety.domain.com.poi.controller;

import com.likeyou.safety.domain.com.poi.dto.PoiResponse;
import com.likeyou.safety.domain.com.poi.service.PoiService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/com")
@RequiredArgsConstructor
public class PoiController {

    private final PoiService poiService;

    /**
     * 모든 POI 조회
     * GET /admin/com/poi
     */
    @GetMapping("/poi")
    public ApiResponse<List<PoiResponse>> getAllPoi() {
        List<PoiResponse> data = poiService.getAllPoi();
        return ApiResponse.success(data);
    }

    /**
     * 단일 POI 상세 조회
     * GET /admin/com/poi/{poiId}
     */
    @GetMapping("/poi/{poiId}")
    public ApiResponse<PoiResponse> getPoiDetail(@PathVariable UUID poiId) {
        PoiResponse data = poiService.getPoiDetail(poiId);
        return ApiResponse.success(data);
    }
}
