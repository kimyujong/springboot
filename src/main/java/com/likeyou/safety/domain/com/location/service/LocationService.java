package com.likeyou.safety.domain.com.location.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.likeyou.safety.domain.com.location.dto.LocationResponse;
import com.likeyou.safety.domain.com.location.entity.ComLocation;
import com.likeyou.safety.domain.com.location.entity.ComLocationId;
import com.likeyou.safety.domain.com.location.repository.ComLocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final ComLocationRepository locationRepository;

    /**
     * 전체 Location 조회
     */
    public List<LocationResponse> getAllLocations() {
        List<ComLocation> list = locationRepository.findAll();

        return list.stream()
                .map(loc -> LocationResponse.builder()
                        .uniqueRoadId(loc.getUniqueRoadId())
                        .hour(loc.getHour())
                        .osmid(loc.getOsmid())
                        .name(loc.getName())
                        .dong(loc.getDong())
                        .riskScore(loc.getRiskScore())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 단일 Location 상세 조회 (Composite PK: uniqueRoadId + hour)
     */
    public LocationResponse getLocationDetail(Long roadId, Integer hour) {

        ComLocationId id = new ComLocationId(roadId, hour);

        ComLocation loc = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Location입니다."));

        return LocationResponse.builder()
                .uniqueRoadId(loc.getUniqueRoadId())
                .hour(loc.getHour())
                .osmid(loc.getOsmid())
                .name(loc.getName())
                .dong(loc.getDong())
                .riskScore(loc.getRiskScore())
                .build();
    }
}
