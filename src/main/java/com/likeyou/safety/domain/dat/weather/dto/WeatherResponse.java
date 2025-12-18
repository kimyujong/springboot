package com.likeyou.safety.domain.dat.weather.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherResponse {

    private Integer temperature;   // 현재 기온(°C)
    private int humidity;         // 습도(%)
    private String description;   // 흐림/맑음/비 등 간단 설명
    private double windSpeed;     // 풍속(m/s)
    private String icon;          // 아이콘 코드
}
