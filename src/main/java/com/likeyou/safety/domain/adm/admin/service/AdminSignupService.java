package com.likeyou.safety.domain.adm.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.likeyou.safety.domain.adm.admin.dto.AdminApproveRequest;
import com.likeyou.safety.domain.adm.admin.dto.AdminApproveResponse;
import com.likeyou.safety.domain.adm.admin.dto.AdminListResponse;
import com.likeyou.safety.domain.adm.admin.dto.AdminLoginRequest;
import com.likeyou.safety.domain.adm.admin.dto.AdminLoginResponse;
import com.likeyou.safety.domain.adm.admin.dto.AdminSignupRequest;
import com.likeyou.safety.domain.adm.admin.dto.AdminSignupResponse;
import com.likeyou.safety.domain.adm.admin.entity.Admin;
import com.likeyou.safety.domain.adm.admin.entity.AdminStatus;
import com.likeyou.safety.domain.adm.admin.repository.AdminRepository;
import com.likeyou.safety.global.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSignupService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public AdminSignupResponse signup(AdminSignupRequest request) {

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Admin admin = Admin.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .organization(request.getOrganization())
                .position(request.getPosition())
                .number(request.getNumber())
                .type(request.getType())
                .status(AdminStatus.PENDING)
                .build();

        Admin saved = adminRepository.save(admin);

        return new AdminSignupResponse(
                saved.getAdminId(),
                saved.getEmail(),
                saved.getStatus().name()
        );
    }

    /**
     * 로그인
     */
    @Transactional
    public AdminLoginResponse login(AdminLoginRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 계정입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 계정 상태 체크
        if (admin.getStatus() == AdminStatus.PENDING) {
            throw new IllegalStateException("승인 대기중인 계정입니다.");
        }

        if (admin.getStatus() == AdminStatus.REJECT) {
            throw new IllegalStateException("승인 거부된 계정입니다.");
        }

        if (admin.getStatus() == AdminStatus.HOLD) {
            throw new IllegalStateException("일시 중지된 계정입니다.");
        }

        if (admin.getStatus() == AdminStatus.REVOKED) {
            throw new IllegalStateException("권한이 박탈된 계정입니다.");
        }

        // 🔥 JWT 생성
        String token = jwtTokenProvider.createToken(
                admin.getEmail(),
                admin.getType().name()
        );

        // 🔥 로그인 시 DB에 토큰 & 시간 업데이트
        admin.updateJwtToken(token);   // JWT 저장
        admin.updateTokenIssuedAtNow();      // 토큰 발급 시간
        adminRepository.save(admin);

        return AdminLoginResponse.builder()
                .adminId(admin.getAdminId())
                .email(admin.getEmail())
                .role(admin.getType().name())
                .token(token)
                .build();
    }

    /**
     * 관리자 승인
     */
    @Transactional
    public AdminApproveResponse approveAdmin(AdminApproveRequest request) {

        Admin target = adminRepository.findById(request.getTargetAdminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        Admin approver = adminRepository.findById(request.getApproverId())
                .orElseThrow(() -> new IllegalArgumentException("승인 권한이 있는 사용자가 아닙니다."));

        // SYSTEM만 승인 가능
        if (!approver.getType().name().equals("SYSTEM")) {
            throw new IllegalStateException("승인 권한이 없습니다.");
        }

        // 상태 변경
        target.updateStatus(AdminStatus.valueOf(request.getStatus()));

        // 승인 정보 기록
        target.setApprover(approver);
        target.setApprovedAtNow();

        adminRepository.save(target);

        return AdminApproveResponse.builder()
                .adminId(target.getAdminId())
                .email(target.getEmail())
                .status(target.getStatus().name())
                .approverId(approver.getAdminId())
                .build();
    }

    /**
     * 관리자 목록 조회
     */
    @Transactional
    public List<AdminListResponse> getAdminList() {

        return adminRepository.findAll().stream()
                .map(a -> AdminListResponse.builder()
                        .adminId(a.getAdminId())
                        .email(a.getEmail())
                        .name(a.getName())
                        .organization(a.getOrganization())
                        .position(a.getPosition())
                        .number(a.getNumber())
                        .type(a.getType().name())
                        .status(a.getStatus().name())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
