package com.likeyou.safety.domain.com.location.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ComLocationId implements Serializable {

    private Long uniqueRoadId;
    private Integer hour;

    public ComLocationId(Long uniqueRoadId, Integer hour) {
        this.uniqueRoadId = uniqueRoadId;
        this.hour = hour;
    }
}
