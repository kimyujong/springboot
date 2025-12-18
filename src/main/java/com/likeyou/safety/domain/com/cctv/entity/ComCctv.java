package com.likeyou.safety.domain.com.cctv.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"COM_CCTV\"")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComCctv {

    @Id
    @Column(name = "cctv_no")
    private UUID cctvNo;

    @Column(name = "cctv_addr")
    private String cctvAddr;

    @Column(name = "cctv_idx")
    private String cctvIdx;

    private Double latitude;

    private Double longitude;

    @Column(name = "stream_url")
    private String streamUrl;

    private String status;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}