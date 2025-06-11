package com.gear4camp.security;
import com.gear4camp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.gear4camp.config.ExcludeUrlConfig.EXCLUDE_URLS;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter : 필터 1회 실행

    private final JwtUtil jwtUtil;

    // 예외 경로 목록 추가(토큰 검사 없이 통과)
//    private static final List<String> EXCLUDE_URLS = List.of(
//            "/auth",
//            "/users/register",
//            "/swagger-ui",
//            "/v3/api-docs"
//    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 회원가입 시
        if(EXCLUDE_URLS.stream().anyMatch(requestURI::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        /*if (EXCLUDE_URLS.stream().anyMatch(requestURI::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }*/
        /*if (requestURI.startsWith("/auth") || requestURI.equals("/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }*/

        // 1. Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // "Bearer " 이후부터 자름

        // 2. 토큰 유효성 검증
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 사용자 ID 추출
        String userId = jwtUtil.getUserIdFromToken(token);

        // 4. 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, null); // 권한은 일단 null

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 5. SecurityContext에 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}