package com.likeyou.safety.domain.app.sos.service;

import com.likeyou.safety.domain.app.user.entity.AppUserInfo;
import com.likeyou.safety.domain.app.user.repository.AppUserInfoRepository;
import com.likeyou.safety.domain.app.sos.dto.AppSosRequest;
import com.likeyou.safety.domain.app.sos.dto.AppSosResponse;
import com.likeyou.safety.domain.app.sos.entity.AppSosLog;
import com.likeyou.safety.domain.app.sos.repository.AppSosLogRepository;
import com.likeyou.safety.global.websocket.DashboardWebSocketController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AppSosService {

    private final AppSosLogRepository appSosLogRepository;
    private final AppUserInfoRepository appUserInfoRepository;  // ✅ 이 줄 추가
    private final DashboardWebSocketController webSocketController;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 시민 SOS 요청 저장
     */
    @Transactional
    public AppSosResponse createSos(AppSosRequest request) {
        UUID userUuid;
        try {
            userUuid = UUID.fromString(request.getUserUuid());
        } catch (Exception e) {
            userUuid = UUID.randomUUID();
            log.warn("[SOS] 유효하지 않은 userUuid, 임시 생성: {}", userUuid);
        }
    
        // ✅ 사용자가 없으면 자동 생성 (FK 제약조건 충족)
        if (!appUserInfoRepository.existsById(userUuid)) {
            AppUserInfo newUser = AppUserInfo.builder()
                    .userUuid(userUuid)
                    .build();
            appUserInfoRepository.save(newUser);
            log.info("[SOS] 새 사용자 자동 생성: {}", userUuid);
        }
    
        AppSosLog sosLog = AppSosLog.builder()
                .userUuid(userUuid)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    
        AppSosLog saved = appSosLogRepository.save(sosLog);

        log.info("[SOS] 시민 SOS 접수: id={}, lat={}, lng={}", 
                saved.getSosLogId(), saved.getLatitude(), saved.getLongitude());

        // WebSocket으로 관리자 대시보드에 실시간 알림
        webSocketController.sendSOSAlert(Map.of(
                "type", "sos",
                "id", saved.getSosLogId().toString(),
                "latitude", saved.getLatitude(),
                "longitude", saved.getLongitude(),
                "status", saved.getStatus(),
                "time", saved.getReceivedAt().format(FORMATTER)
        ));

        return AppSosResponse.builder()
                .id(saved.getSosLogId().toString())
                .status(saved.getStatus())
                .message("SOS 요청이 접수되었습니다.")
                .createdAt(saved.getReceivedAt().format(FORMATTER))
                .build();
    }

    /**
     * SOS 상태 조회
     */
    public AppSosResponse getSosStatus(String sosId) {
        UUID sosLogId = UUID.fromString(sosId);
        
        return appSosLogRepository.findById(sosLogId)
                .map(sos -> AppSosResponse.builder()
                        .id(sos.getSosLogId().toString())
                        .status(sos.getStatus())
                        .message(getStatusMessage(sos.getStatus()))
                        .createdAt(sos.getReceivedAt().format(FORMATTER))
                        .build())
                .orElse(AppSosResponse.builder()
                        .id(sosId)
                        .status("NOT_FOUND")
                        .message("요청을 찾을 수 없습니다.")
                        .build());
    }

    private String getStatusMessage(String status) {
        return switch (status) {
            case "RECEIVED" -> "SOS 요청이 접수되었습니다.";
            case "ACCEPT" -> "관리자가 출동 중입니다.";
            case "DONE" -> "처리가 완료되었습니다.";
            default -> "상태 확인 중...";
        };
    }
}