package com.likeyou.safety.domain.com.poi.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.likeyou.safety.domain.com.poi.dto.PoiResponse;
import com.likeyou.safety.domain.com.poi.entity.ComPoi;
import com.likeyou.safety.domain.com.poi.repository.ComPoiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PoiService {

    private final ComPoiRepository poiRepository;

    /**
     * 모든 POI 조회
     */
    public List<PoiResponse> getAllPoi() {
        List<ComPoi> list = poiRepository.findAll();

        return list.stream()
                .map(p -> PoiResponse.builder()
                        .poiId(p.getPoiId().toString())
                        .type(p.getType())
                        .name(p.getName())
                        .latitude(p.getLatitude())
                        .longitude(p.getLongitude())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 특정 POI 상세 조회
     */
    public PoiResponse getPoiDetail(UUID poiId) {
        ComPoi poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 POI입니다."));

        return PoiResponse.builder()
                .poiId(poi.getPoiId().toString())
                .type(poi.getType())
                .name(poi.getName())
                .latitude(poi.getLatitude())
                .longitude(poi.getLongitude())
                .build();
    }
}
