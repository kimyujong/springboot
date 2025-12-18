package com.likeyou.safety.domain.dat.prediction.service;

import com.likeyou.safety.domain.dat.prediction.dto.*;
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
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;

    /**
     * M5 단일 예측
     */
    public PredictionResponse predict(PredictionRequest request) {
        String url = fastApiConfig.getM5Url() + "/m5/predict";
        
        log.info("[M5] 예측 요청: region={}, date={}", 
                request.getRegionCode(), request.getTargetDate());
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PredictionRequest> entity = new HttpEntity<>(request, headers);
            
            PredictionResponse response = restTemplate.postForObject(url, entity, PredictionResponse.class);
            
            if (response != null) {
                log.info("[M5] 예측 성공: {} 시간대 데이터", response.getData().size());
                return response;
            }
        } catch (RestClientException e) {
            log.error("[M5] FastAPI 호출 실패: {}", e.getMessage());
        }
        
        return PredictionResponse.builder()
                .meta(Map.of("error", "M5 서버 연결 실패"))
                .weather(WeatherSummary.builder().condition("unknown").build())
                .data(Collections.emptyList())
                .build();
    }

    /**
     * M5 현재 날씨 조회
     */
    public CurrentWeatherResponse getCurrentWeather(Integer regionCode) {
        String url = fastApiConfig.getM5Url() + "/m5/weather/current?region_code=" + regionCode;
        
        log.info("[M5] 현재 날씨 조회: region={}", regionCode);
        
        try {
            CurrentWeatherResponse response = restTemplate.getForObject(url, CurrentWeatherResponse.class);
            
            if (response != null) {
                log.info("[M5] 날씨 조회 성공: {}°C, {}", response.getTemp(), response.getConditionText());
                return response;
            }
        } catch (RestClientException e) {
            log.error("[M5] 날씨 조회 실패: {}", e.getMessage());
        }
        
        return CurrentWeatherResponse.builder()
                .temp(0.0)
                .conditionText("정보없음")
                .conditionCode(0)
                .rainAmount(0.0)
                .baseTime("N/A")
                .build();
    }

    /**
     * M5 헬스체크
     */
    public boolean checkHealth() {
        String url = fastApiConfig.getM5Url() + "/";
        
        try {
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (RestClientException e) {
            log.warn("[M5] Health check 실패: {}", e.getMessage());
            return false;
        }
    }
}