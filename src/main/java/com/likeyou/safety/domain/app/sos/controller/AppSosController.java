package com.likeyou.safety.domain.app.sos.controller;

import com.likeyou.safety.domain.app.sos.dto.AppSosRequest;
import com.likeyou.safety.domain.app.sos.dto.AppSosResponse;
import com.likeyou.safety.domain.app.sos.service.AppSosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sos")
@RequiredArgsConstructor
@Tag(name = "Citizen SOS", description = "시민 앱 SOS API")
public class AppSosController {

    private final AppSosService appSosService;

    /**
     * SOS 요청 전송
     * POST /api/sos
     */
    @PostMapping
    @Operation(summary = "SOS 요청 전송", description = "시민 앱에서 긴급 SOS 요청을 전송합니다.")
    public ResponseEntity<AppSosResponse> sendSos(@RequestBody AppSosRequest request) {
        AppSosResponse response = appSosService.createSos(request);
        return ResponseEntity.ok(response);
    }

    /**
     * SOS 상태 조회
     * GET /api/sos/{sosId}
     */
    @GetMapping("/{sosId}")
    @Operation(summary = "SOS 상태 조회", description = "SOS 요청의 현재 상태를 조회합니다.")
    public ResponseEntity<AppSosResponse> getSosStatus(@PathVariable String sosId) {
        AppSosResponse response = appSosService.getSosStatus(sosId);
        return ResponseEntity.ok(response);
    }
}