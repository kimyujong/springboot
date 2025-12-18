package com.likeyou.safety.domain.dat.sos.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SosSummaryResponse {

    private final int totalCount;      // 전체 SOS 요청
    private final int pendingCount;    // 처리 대기
    private final int resolvedCount;   // 처리 완료
}
