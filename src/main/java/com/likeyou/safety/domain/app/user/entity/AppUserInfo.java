package com.likeyou.safety.domain.app.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"APP_User_Info\"")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserInfo {

    @Id
    @Column(name = "user_uuid")
    private UUID userUuid;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "last_active_at")
    private OffsetDateTime lastActiveAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}