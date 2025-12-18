package com.likeyou.safety.domain.dat.sos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneStatResponse {
    private String region;
    private int count;
}