package com.likeyou.safety.domain.app.user.repository;

import com.likeyou.safety.domain.app.user.entity.AppUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserInfoRepository extends JpaRepository<AppUserInfo, UUID> {
}