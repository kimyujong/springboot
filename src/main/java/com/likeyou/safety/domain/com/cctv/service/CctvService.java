package com.likeyou.safety.domain.com.cctv.service;

import com.likeyou.safety.domain.com.cctv.dto.CctvResponse;
import com.likeyou.safety.domain.com.cctv.dto.CctvStatusResponse;
import com.likeyou.safety.domain.com.cctv.entity.ComCctv;
import com.likeyou.safety.domain.com.cctv.repository.ComCctvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CctvService {

    private final ComCctvRepository cctvRepository;

    /**
     * 모든 CCTV 조회 (지도용)
     */
    public List<CctvResponse> getAllCctv() {
        List<ComCctv> list = cctvRepository.findAll();

        return list.stream()
                .map(c -> CctvResponse.builder()
                        .address(c.getCctvAddr())
                        .cctvIdx(c.getCctvIdx())
                        .latitude(c.getLatitude() != null ? c.getLatitude() : 0.0)
                        .longitude(c.getLongitude() != null ? c.getLongitude() : 0.0)
                        .status(c.getStatus())
                        .active(c.getIsActive() != null && c.getIsActive())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 단일 CCTV 상세 조회
     */
    public CctvResponse getCctvDetail(UUID cctvId) {
        ComCctv c = cctvRepository.findById(cctvId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 CCTV입니다."));

        return CctvResponse.builder()
                .address(c.getCctvAddr())
                .cctvIdx(c.getCctvIdx())
                .latitude(c.getLatitude() != null ? c.getLatitude() : 0.0)
                .longitude(c.getLongitude() != null ? c.getLongitude() : 0.0)
                .status(c.getStatus())
                .active(c.getIsActive() != null && c.getIsActive())
                .build();
    }

    /**
     * CCTV 상태 모달용 전체 상태 목록 조회
     */
    public List<CctvStatusResponse> getAllCctvStatus() {
        List<ComCctv> list = cctvRepository.findAll();

        return list.stream()
                .map(c -> CctvStatusResponse.builder()
                        .address(c.getCctvAddr())
                        .cctvIdx(c.getCctvIdx())
                        .status(c.getStatus())
                        .updatedAt(c.getUpdatedAt() == null ? null : c.getUpdatedAt().toString())
                        .build()
                )
                .collect(Collectors.toList());
    }
}