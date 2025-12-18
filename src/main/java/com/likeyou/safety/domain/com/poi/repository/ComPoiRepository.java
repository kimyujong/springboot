package com.likeyou.safety.domain.com.poi.repository;

import com.likeyou.safety.domain.com.poi.entity.ComPoi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ComPoiRepository extends JpaRepository<ComPoi, UUID> {

    // 타입별 조회 (예: TOILET, EMERGENCY, INFO_BOOTH)
    List<ComPoi> findByType(String type);

}
