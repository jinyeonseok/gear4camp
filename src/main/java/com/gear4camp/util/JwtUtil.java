package com.gear4camp.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "gear4campgear4campgear4campgear4camp"; // 최소 32byte
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 🔐 토큰 생성
    public String createToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(userId)                 // 사용자 정보 (주체)
                .issuedAt(now)                  // 발급 시간
                .expiration(expiry)             // 만료 시간
                .signWith(key, Jwts.SIG.HS256) // 서명
                .compact();
    }

    // 토큰에서 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();  // sub 필드에 userId 저장됨
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 내부용 Claim 파싱
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Authentication 객체에서 userId 추출하는 유틸 메서드
    public static String getUserIdFromAuthentication(Authentication authentication) {
        return authentication.getName(); // JwtAuthenticationFilter에서 userId를 set해줌
    }
}
