package com.likeyou.safety.domain.com.location.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "\"COM_Location\"")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ComLocationId.class)
public class ComLocation {

    @Id
    @Column(name = "unique_road_id")
    private Long uniqueRoadId;

    @Id
    @Column(name = "hour")
    private Integer hour;

    // ✅ 수정: JSONB 타입 명시
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String osmid;

    private String name;

    private String dong;

    @Column(name = "risk_score")
    private Double riskScore;
}