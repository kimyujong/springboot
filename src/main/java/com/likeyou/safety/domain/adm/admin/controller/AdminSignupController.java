package com.likeyou.safety.domain.adm.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likeyou.safety.domain.adm.admin.dto.AdminSignupRequest;
import com.likeyou.safety.domain.adm.admin.service.AdminSignupService;

@RestController
@RequestMapping("admin")
public class AdminSignupController {

    private final AdminSignupService adminSignupService;

    public AdminSignupController(AdminSignupService adminSignupService) {
        this.adminSignupService = adminSignupService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody AdminSignupRequest request) {
        return ResponseEntity.ok(adminSignupService.signup(request));
    }
}
