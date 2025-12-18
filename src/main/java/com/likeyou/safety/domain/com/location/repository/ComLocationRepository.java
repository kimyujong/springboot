package com.likeyou.safety.domain.com.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likeyou.safety.domain.com.location.entity.ComLocation;
import com.likeyou.safety.domain.com.location.entity.ComLocationId;

public interface ComLocationRepository extends JpaRepository<ComLocation, ComLocationId> {
    // 도로 ID 기준 전체 시간대 조회
    List<ComLocation> findByUniqueRoadId(Long uniqueRoadId);
    
    // 특정 시간대 단일 조회
    ComLocation findByUniqueRoadIdAndHour(Long uniqueRoadId, Integer hour);
}
