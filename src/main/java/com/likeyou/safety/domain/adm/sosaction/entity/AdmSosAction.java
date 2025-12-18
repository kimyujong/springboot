package com.likeyou.safety.domain.adm.sosaction.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"ADM_SOS_Action\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AdmSosActionId.class)
public class AdmSosAction {

    @Id
    @Column(name = "admin_id")
    private Long adminId;

    @Id
    @Column(name = "sos_log_id")
    private UUID sosLogId;

    @Column(nullable = false)
    private String action;  // 'RECEIVED' | 'ACCEPT' | 'DONE'

    @Column(name = "received_at")
    private OffsetDateTime receivedAt;

    @Column(name = "done_at")
    private OffsetDateTime doneAt;
}