package com.likeyou.safety.domain.adm.admin.controller;

import com.likeyou.safety.domain.adm.admin.dto.AdminListResponse;
import com.likeyou.safety.domain.adm.admin.service.AdminSignupService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminListController {

    private final AdminSignupService adminService;

    @GetMapping("/auth/list")
    public ApiResponse<List<AdminListResponse>> getAdminList() {
        List<AdminListResponse> list = adminService.getAdminList();
        return ApiResponse.success(list);
    }
}
