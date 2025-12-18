package com.likeyou.safety.domain.adm.sosaction.entity;

import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdmSosActionId implements Serializable {
    private Long adminId;
    private UUID sosLogId;
}