// package com.likeyou.safety.global.security.jwt;

// import java.security.Key;
// import java.util.Date;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class JwtTokenProvider {

//     @Value("${jwt.secret}")
//     private String secretKey;

//     @Value("${jwt.expiration}")
//     private long expirationMs;

//     private Key getSigningKey() {
//         return Keys.hmacShaKeyFor(secretKey.getBytes());  
//         // Base64 문자열에서 32바이트 키 생성
//     }

//     // JWT 생성
//     public String createToken(String email, String role) {
//         Date now = new Date();
//         Date expiry = new Date(now.getTime() + expirationMs);

//         return Jwts.builder()
//                 .subject(email)
//                 .claim("role", role)
//                 .issuedAt(now)
//                 .expiration(expiry)
//                 // .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                 .signWith(key)
//                 .compact();
//     }

//     // 토큰에서 email 추출
//     public String getEmailFromToken(String token) {
//         return Jwts.parser()
//                 .setSigningKey(getSigningKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody()
//                 .getSubject();
//     }

//     // 토큰 만료 여부
//     private boolean isTokenExpired(String token) {
//         Date expiration = Jwts.parser()
//                 .setSigningKey(getSigningKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody()
//                 .getExpiration();

//         return expiration.before(new Date());
//     }

//     // 토큰 유효성 검증
//     public boolean validateToken(String token, UserDetails userDetails) {
//         try {
//             String email = getEmailFromToken(token);
//             return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
//         } catch (Exception e) {
//             return false;
//         }
//     }
// }

package com.likeyou.safety.global.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // JWT 생성
    public String createToken(String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)                    
                .claim("role", role)
                .issuedAt(now)                     
                .expiration(expiry)               
                .signWith(getSigningKey()) 
                .compact();
    }

    // 토큰에서 email 추출
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 토큰 만료 여부
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String email = getEmailFromToken(token);
            return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}