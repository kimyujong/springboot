package com.likeyou.safety.domain.dat.risk.service;

import com.likeyou.safety.domain.dat.risk.dto.RiskResponse;
import com.likeyou.safety.global.config.FastApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskService {

    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;

    /**
     * M1 FastAPI에서 특정 시간대의 도로 위험도를 조회합니다.
     * 
     * @param hour 조회할 시간대 (0~23)
     * @return 도로 위험도 응답
     */
    public RiskResponse getRoadRisk(int hour) {
        String url = fastApiConfig.getM1Url() + "/m1/risk?hour=" + hour;
        
        log.info("[M1] 도로 위험도 조회 요청: hour={}", hour);
        
        try {
            RiskResponse response = restTemplate.getForObject(url, RiskResponse.class);
            
            if (response != null) {
                log.info("[M1] 도로 위험도 조회 성공: {} 건", response.getCount());
                return response;
            }
        } catch (RestClientException e) {
            log.error("[M1] FastAPI 호출 실패: {}", e.getMessage());
        }
        
        // Fallback: 빈 응답 반환
        return RiskResponse.builder()
                .hour(hour)
                .count(0)
                .data(Collections.emptyList())
                .build();
    }

    /**
     * M1 FastAPI 서버 헬스체크
     */
    public boolean checkHealth() {
        String url = fastApiConfig.getM1Url() + "/health";
        
        try {
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (RestClientException e) {
            log.warn("[M1] Health check 실패: {}", e.getMessage());
            return false;
        }
    }
}