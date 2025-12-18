package com.likeyou.safety.domain.adm.admin.dto;

import com.likeyou.safety.domain.adm.admin.entity.AdminType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminSignupRequest {

    private String email;
    private String password;
    private String name;
    private String organization;
    private String position;
    private String number;
    private AdminType type;   // SYSTEM / ADMIN / GOV
}
