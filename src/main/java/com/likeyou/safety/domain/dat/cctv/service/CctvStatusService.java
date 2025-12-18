package com.likeyou.safety.domain.dat.cctv.service;

import com.likeyou.safety.domain.dat.cctv.dto.CctvStatusSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CctvStatusService {

    public CctvStatusSummaryResponse getStatusSummary() {

        // TODO: 현재는 프론트 테스트용 더미 값 프론트 연동 시 재시도하기
        int active = 62;
        int inactive = 12;
        int maintenance = 3;

        return CctvStatusSummaryResponse.builder()
                .activeCount(active)
                .inactiveCount(inactive)
                .maintenanceCount(maintenance)
                .build();
    }
}
