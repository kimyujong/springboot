package com.likeyou.safety.domain.adm.sosaction.repository;

import com.likeyou.safety.domain.adm.sosaction.entity.AdmSosAction;
import com.likeyou.safety.domain.adm.sosaction.entity.AdmSosActionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdmSosActionRepository extends JpaRepository<AdmSosAction, AdmSosActionId> {

    /**
     * 특정 SOS에 대한 모든 액션 조회
     */
    List<AdmSosAction> findBySosLogId(UUID sosLogId);

    /**
     * 특정 관리자의 모든 액션 조회
     */
    List<AdmSosAction> findByAdminId(Long adminId);
}