package com.likeyou.safety.domain.adm.admin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likeyou.safety.domain.adm.admin.dto.AdminLoginRequest;
import com.likeyou.safety.domain.adm.admin.dto.AdminLoginResponse;
import com.likeyou.safety.domain.adm.admin.service.AdminSignupService;
import com.likeyou.safety.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminLoginController {

    private final AdminSignupService adminSignupService;

    /**
     * 관리자 로그인
     */
    @PostMapping("/auth/login")
    public ApiResponse<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {

        AdminLoginResponse response = adminSignupService.login(request);

        return ApiResponse.success(response);
    }
}
