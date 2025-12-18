package com.likeyou.safety.domain.dat.sos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SosEventResponse {
    private String id;
    private String time;
    private String type;       // "emergency" | "warning" | "info"
    private String message;
    private String location;
    private boolean isCctv;
    private String status;     // "new" | "dispatched" | "resolved"
    private String responder;
}