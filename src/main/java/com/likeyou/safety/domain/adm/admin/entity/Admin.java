package com.likeyou.safety.domain.adm.admin.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"ADM_Admin_Info\"")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;
    private String organization;
    private String position;
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminStatus status;

    @Column(name = "jwt_token", columnDefinition = "TEXT")
    private String jwtToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Admin approver;

    @Column(name = "approved_at")      // ⬅︎ 수정됨
    private OffsetDateTime approvedAt;

    @Column(name = "token_issued_at")  // ⬅︎ 필드 위치 정상화
    private OffsetDateTime tokenIssuedAt;


    /* ==========================
        🔥 Domain Methods
       ========================== */

    public void updateStatus(AdminStatus newStatus) {
        this.status = newStatus;
    }

    public void setApprover(Admin approver) {
        this.approver = approver;
    }

    public void setApprovedAtNow() {
        this.approvedAt = OffsetDateTime.now();
    }

    public void updateJwtToken(String token) {
        this.jwtToken = token;
    }

    public void updateTokenIssuedAtNow() {
        this.tokenIssuedAt = OffsetDateTime.now();
    }
}
