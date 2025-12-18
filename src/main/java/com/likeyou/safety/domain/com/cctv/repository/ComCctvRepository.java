package com.likeyou.safety.domain.com.cctv.repository;

import com.likeyou.safety.domain.com.cctv.entity.ComCctv;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ComCctvRepository extends JpaRepository<ComCctv, UUID> {

    // 활성 CCTV만 조회 (is_active = true)
    List<ComCctv> findByIsActiveTrue();

    // 상태별 조회 (ACTIVE / INACTIVE)
    List<ComCctv> findByStatus(String status);

}
