package com.likeyou.safety.domain.adm.admin.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.likeyou.safety.domain.adm.admin.dto.AdminApproveRequest;
import com.likeyou.safety.domain.adm.admin.dto.AdminApproveResponse;
import com.likeyou.safety.domain.adm.admin.service.AdminSignupService;
import com.likeyou.safety.global.response.ApiResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminApproveController {

    private final AdminSignupService adminService;

    @PatchMapping("/auth/approve")
    public ApiResponse<AdminApproveResponse> approve(@RequestBody AdminApproveRequest request) {

        AdminApproveResponse response = adminService.approveAdmin(request);
        return ApiResponse.success(response);  // ← 여기 success()가 정석
    }
}
