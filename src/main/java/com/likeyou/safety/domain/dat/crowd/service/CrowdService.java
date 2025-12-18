package com.likeyou.safety.domain.dat.crowd.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.likeyou.safety.domain.dat.crowd.dto.CctvDensityResponse;
import com.likeyou.safety.domain.dat.crowd.dto.CrowdAlertResponse;
import com.likeyou.safety.domain.dat.crowd.dto.CrowdSummaryResponse;
import com.likeyou.safety.domain.com.cctv.entity.ComCctv;
import com.likeyou.safety.domain.com.cctv.repository.ComCctvRepository;
import com.likeyou.safety.global.config.FastApiConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrowdService {

    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;
    private final ComCctvRepository cctvRepository;  // ✅ 추가!

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * M3 FastAPI에서 분석 로그를 조회하여 혼잡도 요약을 생성합니다.
     */
    public CrowdSummaryResponse getCrowdSummary() {
        String url = fastApiConfig.getM3Url() + "/logs?limit=20";
        
        log.info("[M3] 혼잡도 로그 조회 요청");
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && "success".equals(response.get("status"))) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> logs = (List<Map<String, Object>>) response.get("data");
                
                log.info("[M3] 로그 조회 성공: {} 건", logs.size());
                
                // 로그 데이터를 CctvDensityResponse로 변환
                List<CctvDensityResponse> cctvDensity = new ArrayList<>();
                List<CrowdAlertResponse> crowdAlerts = new ArrayList<>();
                
                for (Map<String, Object> logItem : logs) {
                    // CCTV 밀집도 정보
                    String cctvNoStr = (String) logItem.getOrDefault("cctv_no", null);
                    Integer personCount = (Integer) logItem.getOrDefault("person_count", 0);
                    Integer congestionLevel = (Integer) logItem.getOrDefault("congestion_level", 0);
                    Integer riskLevel = (Integer) logItem.getOrDefault("risk_level", 1);
                    String timestamp = (String) logItem.getOrDefault("created_at", nowString());
                    
                    // ✅ UUID로 COM_CCTV 조회해서 실제 cctvIdx, address 가져오기
                    String cctvIdx = "UNKNOWN";
                    String address = "알 수 없음";
                    
                    if (cctvNoStr != null) {
                        try {
                            UUID cctvNo = UUID.fromString(cctvNoStr);
                            Optional<ComCctv> cctvOpt = cctvRepository.findById(cctvNo);
                            if (cctvOpt.isPresent()) {
                                ComCctv cctv = cctvOpt.get();
                                cctvIdx = cctv.getCctvIdx() != null ? cctv.getCctvIdx() : "CCTV-" + cctvNo.toString().substring(0, 4);
                                address = cctv.getCctvAddr() != null ? cctv.getCctvAddr() : "주소 미등록";
                                log.debug("[M3] CCTV 매핑 성공: {} -> idx={}, addr={}", cctvNoStr, cctvIdx, address);
                            } else {
                                log.warn("[M3] COM_CCTV에서 찾을 수 없음: {}", cctvNoStr);
                            }
                        } catch (IllegalArgumentException e) {
                            log.warn("[M3] 유효하지 않은 UUID: {}", cctvNoStr);
                        }
                    }
                    
                    // 상태 결정
                    String status = "active";
                    if (riskLevel >= 4) status = "danger";
                    else if (riskLevel >= 3) status = "warning";
                    
                    cctvDensity.add(CctvDensityResponse.builder()
                            .cctvIdx(cctvIdx)         // ✅ 실제 cctvIdx
                            .address(address)         // ✅ 실제 address
                            .status(status)
                            .density(congestionLevel)
                            .updatedAt(timestamp)
                            .build());
                    
                    // 위험 등급이 높으면 경보로 추가
                    if (riskLevel >= 3) {
                        String alertType = riskLevel >= 4 ? "danger" : "warning";
                        String message = String.format("%s 혼잡도 %d%% (인원: %d명)", 
                                address, congestionLevel, personCount);
                        
                        crowdAlerts.add(CrowdAlertResponse.builder()
                                .id((long) crowdAlerts.size() + 1)
                                .type(alertType)
                                .message(message)
                                .time(timestamp)
                                .location(address)
                                .status("active")
                                .build());
                    }
                }
                
                return CrowdSummaryResponse.builder()
                        .cctvDensity(cctvDensity)
                        .crowdAlerts(crowdAlerts)
                        .build();
            }
        } catch (RestClientException e) {
            log.error("[M3] FastAPI 호출 실패: {}", e.getMessage());
        }
        
        // Fallback: 빈 응답
        return CrowdSummaryResponse.builder()
                .cctvDensity(Collections.emptyList())
                .crowdAlerts(Collections.emptyList())
                .build();
    }

    /**
     * M3 분석 시작
     */
    public Map<String, Object> startAnalysis(String cctvIdx, String videoPath) {
        String url = fastApiConfig.getM3Url() + "/control/start?cctv_idx=" + cctvIdx;
        if (videoPath != null) {
            url += "&video_path=" + videoPath;
        }
        
        log.info("[M3] 분석 시작 요청: {}", cctvIdx);
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
            return response != null ? response : Map.of("status", "error");
        } catch (RestClientException e) {
            log.error("[M3] 분석 시작 실패: {}", e.getMessage());
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    /**
     * M3 분석 중지
     */
    public Map<String, Object> stopAnalysis(String cctvIdx) {
        String url = fastApiConfig.getM3Url() + "/control/stop?cctv_idx=" + cctvIdx;
        
        log.info("[M3] 분석 중지 요청: {}", cctvIdx);
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
            return response != null ? response : Map.of("status", "error");
        } catch (RestClientException e) {
            log.error("[M3] 분석 중지 실패: {}", e.getMessage());
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    /**
     * M3 헬스체크
     */
    public boolean checkHealth() {
        String url = fastApiConfig.getM3Url() + "/health";
        
        try {
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (RestClientException e) {
            log.warn("[M3] Health check 실패: {}", e.getMessage());
            return false;
        }
    }

    private String nowString() {
        return OffsetDateTime.now().format(FORMATTER);
    }
}