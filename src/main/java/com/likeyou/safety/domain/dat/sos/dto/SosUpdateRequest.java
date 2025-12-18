package com.likeyou.safety.domain.dat.sos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SosUpdateRequest {
    private String status;     // "dispatched" | "resolved"
    private String responder;
}