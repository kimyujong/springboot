package com.likeyou.safety.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.likeyou.safety.global.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ✅ CORS 설정 추가
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // REST API 서버에서는 CSRF 필요 없음
            .csrf(csrf -> csrf.disable())

            // 세션 비활성화 → JWT 인증 방식
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // URL별 권한 설정
            .authorizeHttpRequests(auth -> auth
                // ✅ OPTIONS 요청 허용 (Preflight)

                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // ✅ WebSocket 허용 추가
                .requestMatchers("/ws/**").permitAll()
                // ✅ API 요청 허용 추가
                .requestMatchers("/api/**").permitAll()
                .requestMatchers(
                    "/admin/auth/login",
                    "/admin/auth/signup"
                ).permitAll()
                .requestMatchers(
                    "/admin/auth/approve",
                    "/admin/auth/list"
                ).hasAnyRole("SYSTEM", "ADMIN")
                // ✅ CCTV 관련 요청 허용
                .requestMatchers("/admin/dat/cctv/status").authenticated()
                .requestMatchers("/admin/dat/cctv/**").authenticated()
                // 기존 설정들
                .requestMatchers("/admin/com/**")
                    .hasAnyRole("SYSTEM", "ADMIN", "GOV")
                .requestMatchers("/admin/dat/**")
                    .hasAnyRole("SYSTEM", "ADMIN", "GOV")
                .requestMatchers("/admin/dashboard/**")
                    .hasAnyRole("SYSTEM", "ADMIN", "GOV")
                .requestMatchers("/admin/dat/weather/**")
                    .hasAnyRole("SYSTEM", "ADMIN", "GOV")
                .anyRequest().authenticated()
            )

            // Form Login 미사용
            .formLogin(form -> form.disable())

            // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 PasswordEncoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🌐 CORS 설정 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // ✅ 배포 + 로컬 환경 모두 허용
        configuration.setAllowedOrigins(java.util.List.of(
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:5173",
            "https://likeyousafety.cloud"
        ));
        
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setExposedHeaders(java.util.List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}