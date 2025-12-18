package com.likeyou.safety.global.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.likeyou.safety.domain.adm.admin.entity.Admin;
import com.likeyou.safety.domain.adm.admin.repository.AdminRepository;
import com.likeyou.safety.domain.adm.admin.entity.AdminStatus; 
import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class CustomUserDetailsService implements UserDetailsService {

//     private final AdminRepository adminRepository;

//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

//         Admin admin = adminRepository.findByEmail(email)
//                 .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 관리자 없음: " + email));

//         return new AdminUserDetails(admin);
//     }
// }

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 관리자 없음: " + email));

        // 🔥 ACTIVE 여부 검사 (엔티티 기준으로 정확하게)
        if (admin.getStatus() != AdminStatus.ACTIVE) {
            throw new UsernameNotFoundException("승인되지 않은 계정입니다: " + email);
        }

        return new AdminUserDetails(admin);
    }
}

