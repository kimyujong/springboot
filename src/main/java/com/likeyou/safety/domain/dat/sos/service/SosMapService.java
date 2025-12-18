package com.likeyou.safety.domain.dat.sos.service;

import com.likeyou.safety.domain.app.sos.entity.AppSosLog;
import com.likeyou.safety.domain.app.sos.repository.AppSosLogRepository;
import com.likeyou.safety.domain.com.cctv.entity.ComCctv;
import com.likeyou.safety.domain.com.cctv.repository.ComCctvRepository;
import com.likeyou.safety.domain.dat.sos.dto.MapEventResponse;
import com.likeyou.safety.global.config.FastApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SosMapService {

    private final AppSosLogRepository appSosLogRepository;
    private final ComCctvRepository cctvRepository;
    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final int EVENT_TIMEOUT_MINUTES = 10;

    /**
     * 지도에 표시할 이벤트 목록 조회
     * - M4 낙상 이벤트 (10분 이내, 미처리)
     * - Citizen SOS 요청 (10분 이내, 미처리)
     */
    public List<MapEventResponse> getMapEvents() {
        List<MapEventResponse> events = new ArrayList<>();
        OffsetDateTime since = OffsetDateTime.now().minusMinutes(EVENT_TIMEOUT_MINUTES);

        // 1. M4 낙상 이벤트 조회
        events.addAll(getFallEvents(since));

        // 2. Citizen SOS 요청 조회
        events.addAll(getCitizenSosEvents(since));

        // 시간순 정렬 (최신 먼저)
        events.sort((a, b) -> b.getTime().compareTo(a.getTime()));

        log.info("[MapEvents] 총 {}건 (낙상: {}, SOS: {})", 
                events.size(),
                events.stream().filter(e -> "fall".equals(e.getType())).count(),
                events.stream().filter(e -> "sos".equals(e.getType())).count());

        return events;
    }

    /**
     * M4 낙상 이벤트 조회 (FastAPI + COM_CCTV 좌표)
     */
    private List<MapEventResponse> getFallEvents(OffsetDateTime since) {
        List<MapEventResponse> events = new ArrayList<>();
        String url = fastApiConfig.getM4Url() + "/events?limit=50";

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.get("data") != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> fallEvents = (List<Map<String, Object>>) response.get("data");

                for (Map<String, Object> event : fallEvents) {
                    String status = (String) event.getOrDefault("status", "DETECTED");
                    
                    // RESOLVED는 제외
                    if ("RESOLVED".equalsIgnoreCase(status)) continue;

                    String cctvNoStr = (String) event.getOrDefault("cctv_no", null);
                    String detectedAt = (String) event.getOrDefault("detected_at", "");
                    String eventId = event.get("id") != null ? event.get("id").toString() : "FALL-" + UUID.randomUUID().toString().substring(0, 8);

                    // 10분 이내 체크 (간단하게 시간 비교)
                    // TODO: 실제로는 detectedAt을 파싱해서 시간 비교 필요

                    // CCTV 좌표 조회
                    Double lat = null;
                    Double lng = null;
                    String location = "알 수 없음";

                    if (cctvNoStr != null) {
                        try {
                            UUID cctvNo = UUID.fromString(cctvNoStr);
                            Optional<ComCctv> cctvOpt = cctvRepository.findById(cctvNo);
                            if (cctvOpt.isPresent()) {
                                ComCctv cctv = cctvOpt.get();
                                lat = cctv.getLatitude();
                                lng = cctv.getLongitude();
                                location = cctv.getCctvAddr() != null ? cctv.getCctvAddr() : 
                                          (cctv.getCctvIdx() != null ? cctv.getCctvIdx() : "CCTV");
                            }
                        } catch (Exception e) {
                            log.warn("[MapEvents] CCTV 좌표 조회 실패: {}", cctvNoStr);
                        }
                    }

                    // 좌표가 없으면 스킵
                    if (lat == null || lng == null) continue;

                    events.add(MapEventResponse.builder()
                            .id(eventId)
                            .type("fall")
                            .severity("emergency")
                            .latitude(lat)
                            .longitude(lng)
                            .location(location)
                            .message("낙상 감지")
                            .status(mapFallStatus(status))
                            .time(detectedAt.length() > 8 ? detectedAt.substring(11, 19) : detectedAt)
                            .isCctv(true)
                            .build());
                }
            }
        } catch (RestClientException e) {
            log.error("[MapEvents] M4 낙상 이벤트 조회 실패: {}", e.getMessage());
        }

        return events;
    }

    /**
     * Citizen SOS 요청 조회
     */
    private List<MapEventResponse> getCitizenSosEvents(OffsetDateTime since) {
        List<MapEventResponse> events = new ArrayList<>();

        List<AppSosLog> sosLogs = appSosLogRepository.findActiveRecent(since);

        for (AppSosLog sos : sosLogs) {
            events.add(MapEventResponse.builder()
                    .id(sos.getSosLogId().toString())
                    .type("sos")
                    .severity("emergency")
                    .latitude(sos.getLatitude())
                    .longitude(sos.getLongitude())
                    .location(String.format("%.4f, %.4f", sos.getLatitude(), sos.getLongitude()))
                    .message("시민 SOS 요청")
                    .status(mapSosStatus(sos.getStatus()))
                    .time(sos.getReceivedAt().format(FORMATTER))
                    .isCctv(false)
                    .build());
        }

        return events;
    }

    /**
     * 낙상 상태 매핑
     */
    private String mapFallStatus(String status) {
        return switch (status.toUpperCase()) {
            case "DETECTED" -> "new";
            case "DISPATCHED", "ACCEPT" -> "dispatched";
            case "RESOLVED", "DONE" -> "resolved";
            default -> "new";
        };
    }

    /**
     * SOS 상태 매핑
     */
    private String mapSosStatus(String status) {
        return switch (status.toUpperCase()) {
            case "RECEIVED" -> "new";
            case "ACCEPT" -> "dispatched";
            case "DONE" -> "resolved";
            default -> "new";
        };
    }

    /**
     * 이벤트 상태 업데이트 (출동 처리)
     */
    public boolean updateEventStatus(String eventId, String eventType, String newStatus) {
        if ("sos".equals(eventType)) {
            try {
                UUID sosLogId = UUID.fromString(eventId);
                Optional<AppSosLog> sosOpt = appSosLogRepository.findById(sosLogId);
                if (sosOpt.isPresent()) {
                    AppSosLog sos = sosOpt.get();
                    sos.setStatus(mapToDbStatus(newStatus));
                    if ("dispatched".equals(newStatus)) {
                        sos.setAcceptedAt(OffsetDateTime.now());
                    } else if ("resolved".equals(newStatus)) {
                        sos.setDoneAt(OffsetDateTime.now());
                    }
                    appSosLogRepository.save(sos);
                    log.info("[MapEvents] SOS 상태 업데이트: {} -> {}", eventId, newStatus);
                    return true;
                }
            } catch (Exception e) {
                log.error("[MapEvents] SOS 상태 업데이트 실패: {}", e.getMessage());
            }
        }
        // fall 이벤트는 M4 API 호출 필요 (TODO)
        return false;
    }

    private String mapToDbStatus(String status) {
        return switch (status) {
            case "new" -> "RECEIVED";
            case "dispatched" -> "ACCEPT";
            case "resolved" -> "DONE";
            default -> "RECEIVED";
        };
    }
}