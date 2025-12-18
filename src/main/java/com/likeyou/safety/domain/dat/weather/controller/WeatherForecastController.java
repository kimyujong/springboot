package com.likeyou.safety.domain.dat.weather.controller;

import com.likeyou.safety.domain.dat.weather.dto.WeatherForecastDto;
import com.likeyou.safety.domain.dat.weather.service.WeatherForecastService;
import com.likeyou.safety.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dat/weather")
@RequiredArgsConstructor
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    @GetMapping("/forecast")
    public ApiResponse<List<WeatherForecastDto>> getForecast() throws Exception {
        return ApiResponse.success(weatherForecastService.getShortForecast());
    }
}
