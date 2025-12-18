package com.likeyou.safety.domain.dat.cctv.controller;

import com.likeyou.safety.domain.dat.cctv.dto.CctvStatusSummaryResponse;
import com.likeyou.safety.domain.dat.cctv.service.CctvStatusService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dat/cctv")
public class CctvStatusController {

    private final CctvStatusService cctvStatusService;
    private final CctvService cctvService; // [추가] 기존 CCTV 서비스 재사용

    // [추가] 프론트엔드가 요청하는 URL (/admin/dat/cctv/status) 처리
    @GetMapping("/status")
    public ApiResponse<List<CctvStatusResponse>> getAllCctvStatus() {
        return ApiResponse.success(cctvService.getAllCctvStatus());
    }

    @GetMapping("/status/summary")
    public ApiResponse<CctvStatusSummaryResponse> getSummary() {
        return ApiResponse.success(cctvStatusService.getStatusSummary());
    }
}
