package com.likeyou.safety.domain.app.sos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"APP_SOS_Log\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSosLog {

    @Id
    @Column(name = "sos_log_id")
    private UUID sosLogId;

    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String status;  // 'RECEIVED' | 'ACCEPT' | 'DONE'

    @Column(name = "received_at")
    private OffsetDateTime receivedAt;

    @Column(name = "accepted_at")
    private OffsetDateTime acceptedAt;

    @Column(name = "done_at")
    private OffsetDateTime doneAt;

    @PrePersist
    public void prePersist() {
        if (sosLogId == null) sosLogId = UUID.randomUUID();
        if (status == null) status = "RECEIVED";
        if (receivedAt == null) receivedAt = OffsetDateTime.now();
    }
}