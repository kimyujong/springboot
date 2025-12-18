package com.likeyou.safety.domain.app.sos.repository;

import com.likeyou.safety.domain.app.sos.entity.AppSosLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppSosLogRepository extends JpaRepository<AppSosLog, UUID> {

    /**
     * 최근 N분 이내, 미완료(RECEIVED, ACCEPT) 상태의 SOS 조회
     */
    @Query("SELECT s FROM AppSosLog s WHERE s.status IN ('RECEIVED', 'ACCEPT') AND s.receivedAt >= :since ORDER BY s.receivedAt DESC")
    List<AppSosLog> findActiveRecent(@Param("since") OffsetDateTime since);

    /**
     * 상태별 조회
     */
    List<AppSosLog> findByStatusOrderByReceivedAtDesc(String status);

    /**
     * 최근 N건 조회
     */
    List<AppSosLog> findTop20ByOrderByReceivedAtDesc();
}