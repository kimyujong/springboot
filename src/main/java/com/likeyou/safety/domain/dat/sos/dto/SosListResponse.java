package com.likeyou.safety.domain.dat.sos.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SosListResponse {
    private SosSummaryResponse summary;
    private List<SosItemResponse> recentList;
}
