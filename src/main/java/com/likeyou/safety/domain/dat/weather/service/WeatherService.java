package com.likeyou.safety.domain.dat.weather.service;

import com.likeyou.safety.domain.dat.weather.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.api-key}")
    private String apiKey;

    @Value("${weather.url}")
    private String weatherUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 실시간 날씨 조회 (OpenWeatherMap)
     */
    public WeatherResponse getCurrentWeather(double lat, double lon) {

        String url = UriComponentsBuilder.fromHttpUrl(weatherUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")  // 섭씨 온도 바로 반환됨
                .queryParam("lang", "kr")       // 한국어 설명
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        Map<String, Object> main = (Map<String, Object>) response.get("main");
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        Map<String, Object> weather = ((java.util.List<Map<String, Object>>) response.get("weather")).get(0);

        // 🌡️ 섭씨 온도(Double) → 반올림(Integer)
        double tempCelsius = ((Number) main.get("temp")).doubleValue();
        int roundedTemp = (int) Math.round(tempCelsius);

        return WeatherResponse.builder()
                .temperature(roundedTemp)  // 🔥 반올림 결과 적용
                .humidity(((Number) main.get("humidity")).intValue())
                .description((String) weather.get("description"))
                .windSpeed(((Number) wind.get("speed")).doubleValue())
                .icon((String) weather.get("icon"))
                .build();
    }
}
