package com.likeyou.safety.domain.com.cctv.controller;

import com.likeyou.safety.domain.com.cctv.dto.CctvResponse;
import com.likeyou.safety.domain.com.cctv.dto.CctvStatusResponse;
import com.likeyou.safety.domain.com.cctv.service.CctvService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/com/cctv")
// @RequestMapping("/admin/dat/cctv")
@RequiredArgsConstructor
public class CctvController {

    private final CctvService cctvService;

    /**
     * 모든 CCTV 조회 (지도용)
     */
    @GetMapping
    public ApiResponse<List<CctvResponse>> getAllCctv() {
        return ApiResponse.success(cctvService.getAllCctv());
    }

    /**
     * 단일 CCTV 상세 조회
     */
    @GetMapping("/{cctvId}")
    public ApiResponse<CctvResponse> getCctvDetail(@PathVariable UUID cctvId) {
        return ApiResponse.success(cctvService.getCctvDetail(cctvId));
    }

    /**
     * 모든 CCTV 상태 조회 (대시보드 상태 모달)
     */
    @GetMapping("/status")
    public ApiResponse<List<CctvStatusResponse>> getAllCctvStatus() {
        return ApiResponse.success(cctvService.getAllCctvStatus());
    }
}
