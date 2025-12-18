package com.likeyou.safety.domain.dat.weather.controller;

import com.likeyou.safety.domain.dat.weather.dto.WeatherResponse;
import com.likeyou.safety.domain.dat.weather.service.WeatherService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dat/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * 실시간 날씨 조회
     * GET /admin/dat/weather/current?lat=35.1455&lon=129.1131
     */
    @GetMapping("/current")
    public ApiResponse<WeatherResponse> getCurrentWeather(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon
    ) {
    
        // 기본 좌표 (부산 광안리)
        if (lat == null) lat = 35.1534258039487;
        if (lon == null) lon = 129.11797991542;
    
        WeatherResponse data = weatherService.getCurrentWeather(lat, lon);
        return ApiResponse.success(data);
    }
    
}
