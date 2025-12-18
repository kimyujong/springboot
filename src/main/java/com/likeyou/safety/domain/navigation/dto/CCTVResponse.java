package com.likeyou.safety.domain.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CCTVResponse {
    private Boolean success;
    private List<CCTVPoint> data;
}