package com.likeyou.safety.domain.app.sos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppSosRequest {
    private String userUuid;
    private Double latitude;
    private Double longitude;
    private String message;  // 선택사항
}