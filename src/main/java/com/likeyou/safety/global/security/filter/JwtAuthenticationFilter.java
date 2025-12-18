package com.likeyou.safety.global.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.likeyou.safety.global.security.user.CustomUserDetailsService;
import com.likeyou.safety.global.security.jwt.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Authorization 헤더에서 토큰 가져오기
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtTokenProvider.getEmailFromToken(token);
        }

        // 2️⃣ 토큰이 있고, SecurityContext에 인증이 아직 없는 경우
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 3️⃣ DB에서 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 4️⃣ JWT 토큰 유효성 검증
            if (jwtTokenProvider.validateToken(token, userDetails)) {

                // 5️⃣ 인증 객체 생성
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 6️⃣ SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7️⃣ 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
