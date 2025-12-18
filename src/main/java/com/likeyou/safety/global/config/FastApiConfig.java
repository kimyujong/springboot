package com.likeyou.safety.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class FastApiConfig {

    @Value("${fastapi.m1.url}")
    private String m1Url;

    @Value("${fastapi.m2.url}")
    private String m2Url;

    @Value("${fastapi.m3.url}")
    private String m3Url;

    @Value("${fastapi.m4.url}")
    private String m4Url;

    @Value("${fastapi.m5.url}")
    private String m5Url;
}