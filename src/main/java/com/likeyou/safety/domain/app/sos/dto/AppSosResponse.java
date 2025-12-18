package com.likeyou.safety.domain.app.sos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppSosResponse {
    private String id;
    private String status;
    private String message;
    private String createdAt;
}