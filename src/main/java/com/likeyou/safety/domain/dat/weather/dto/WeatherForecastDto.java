package com.likeyou.safety.domain.dat.weather.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherForecastDto {
    private String time;   // "13:00"
    private String sky;    // "맑음"
    private int temp;      // 10
}
