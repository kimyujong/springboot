package com.likeyou.safety.domain.dat.fall.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class FallSummaryResponse {

    private final int totalCount;
    private final int detectedCount;
    private final int resolvedCount;

    private final List<FallEventDto> recentList;   // 최근 3~5개 리스트
}
