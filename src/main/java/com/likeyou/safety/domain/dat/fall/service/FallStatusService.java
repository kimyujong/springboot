package com.likeyou.safety.domain.dat.fall.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.likeyou.safety.domain.dat.fall.dto.FallEventDto;
import com.likeyou.safety.domain.dat.fall.dto.FallSummaryResponse;
import com.likeyou.safety.global.config.FastApiConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FallStatusService {

    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;

    /**
     * M4 FastAPI에서 낙상 이벤트를 조회하여 요약을 생성합니다.
     */
    public FallSummaryResponse getFallSummary() {
        String url = fastApiConfig.getM4Url() + "/events?limit=20";
        
        log.info("[M4] 낙상 이벤트 조회 요청");
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.get("data") != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> events = (List<Map<String, Object>>) response.get("data");
                
                log.info("[M4] 이벤트 조회 성공: {} 건", events.size());
                
                // 이벤트를 FallEventDto로 변환
                List<FallEventDto> recentList = new ArrayList<>();
                int detectedCount = 0;
                int resolvedCount = 0;
                
                for (Map<String, Object> event : events) {
                    String cctvNo = (String) event.getOrDefault("cctv_no", "UNKNOWN");
                    String status = (String) event.getOrDefault("status", "DETECTED");
                    String detectedAt = (String) event.getOrDefault("detected_at", "");
                    String eventId = (String) event.getOrDefault("id", "FALL-" + recentList.size());
                    
                    if ("DETECTED".equalsIgnoreCase(status)) {
                        detectedCount++;
                    } else {
                        resolvedCount++;
                    }
                    
                    // 최근 5개만 리스트에 추가
                    if (recentList.size() < 5) {
                        recentList.add(FallEventDto.builder()
                                .eventId(eventId.toString())
                                .cctvName(cctvNo)
                                .location(cctvNo + " 구역")
                                .time(detectedAt)
                                .status(status)
                                .build());
                    }
                }
                
                return FallSummaryResponse.builder()
                        .totalCount(events.size())
                        .detectedCount(detectedCount)
                        .resolvedCount(resolvedCount)
                        .recentList(recentList)
                        .build();
            }
        } catch (RestClientException e) {
            log.error("[M4] FastAPI 호출 실패: {}", e.getMessage());
        }
        
        // Fallback: 빈 응답
        return FallSummaryResponse.builder()
                .totalCount(0)
                .detectedCount(0)
                .resolvedCount(0)
                .recentList(Collections.emptyList())
                .build();
    }

    /**
     * M4 분석 시작
     */
    public Map<String, Object> startAnalysis(String cctvIdx, String videoPath) {
        String url = fastApiConfig.getM4Url() + "/control/start?cctv_idx=" + cctvIdx;
        if (videoPath != null) {
            url += "&video_path=" + videoPath;
        }
        
        log.info("[M4] 분석 시작 요청: {}", cctvIdx);
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
            return response != null ? response : Map.of("status", "error");
        } catch (RestClientException e) {
            log.error("[M4] 분석 시작 실패: {}", e.getMessage());
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    /**
     * M4 분석 중지
     */
    public Map<String, Object> stopAnalysis(String cctvNo) {
        String url = fastApiConfig.getM4Url() + "/control/stop?cctv_no=" + cctvNo;
        
        log.info("[M4] 분석 중지 요청: {}", cctvNo);
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
            return response != null ? response : Map.of("status", "error");
        } catch (RestClientException e) {
            log.error("[M4] 분석 중지 실패: {}", e.getMessage());
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    /**
     * M4 헬스체크
     */
    public boolean checkHealth() {
        String url = fastApiConfig.getM4Url() + "/health";
        
        try {
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (RestClientException e) {
            log.warn("[M4] Health check 실패: {}", e.getMessage());
            return false;
        }
    }
}