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

    @GetMapping("/status/summary")
    public ApiResponse<CctvStatusSummaryResponse> getSummary() {
        return ApiResponse.success(cctvStatusService.getStatusSummary());
    }
}
