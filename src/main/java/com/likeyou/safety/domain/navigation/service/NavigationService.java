package com.likeyou.safety.domain.navigation.service;

import com.likeyou.safety.domain.navigation.dto.*;
import com.likeyou.safety.global.config.FastApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class NavigationService {

    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;

    public RouteResponse calculateRoute(RouteRequest request) {
        String url = fastApiConfig.getM2Url() + "/m2/route";
        
        log.info("[M2] 안심 경로 계산 요청: {} -> {}", 
                request.getOrigin(), request.getDestination());
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RouteRequest> entity = new HttpEntity<>(request, headers);
            
            RouteResponse response = restTemplate.postForObject(url, entity, RouteResponse.class);
            
            if (response != null && response.getSuccess()) {
                log.info("[M2] 경로 계산 성공: {} 포인트", response.getPath().size());
                return response;
            }
        } catch (RestClientException e) {
            log.error("[M2] FastAPI 호출 실패: {}", e.getMessage());
        }
        
        return RouteResponse.builder()
                .success(false)
                .path(Collections.emptyList())
                .info(RouteInfo.builder().distance(0.0).durationMin(0).build())
                .error("M2 서버 연결 실패")
                .build();
    }

    public HeatmapResponse getHeatmap() {
        String url = fastApiConfig.getM2Url() + "/m2/heatmap";
        
        try {
            HeatmapResponse response = restTemplate.getForObject(url, HeatmapResponse.class);
            if (response != null && response.getSuccess()) {
                log.info("[M2] 히트맵 조회 성공: {} 포인트", response.getData().size());
                return response;
            }
        } catch (RestClientException e) {
            log.error("[M2] 히트맵 조회 실패: {}", e.getMessage());
        }
        
        return HeatmapResponse.builder()
                .success(false)
                .data(Collections.emptyList())
                .build();
    }

    public CCTVResponse getCCTV() {
        String url = fastApiConfig.getM2Url() + "/m2/cctv";
        
        try {
            CCTVResponse response = restTemplate.getForObject(url, CCTVResponse.class);
            if (response != null && response.getSuccess()) {
                log.info("[M2] CCTV 조회 성공: {} 건", response.getData().size());
                return response;
            }
        } catch (RestClientException e) {
            log.error("[M2] CCTV 조회 실패: {}", e.getMessage());
        }
        
        return CCTVResponse.builder()
                .success(false)
                .data(Collections.emptyList())
                .build();
    }
}