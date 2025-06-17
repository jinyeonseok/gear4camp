package com.gear4camp.controller;

import com.gear4camp.dto.LoginRequestDto;
import com.gear4camp.service.AuthService;
import com.gear4camp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        /*
        // 이 부분은 나중에 사용자 검증 로직 추가 예정
        String userId = loginRequestDto.getUserId();

        // 사용자 인증 성공 시 JWT 발급
        String token = jwtUtil.createToken(userId);
        */

        String token = authService.login(loginRequestDto);

        return ResponseEntity.ok().body(token);
    }
}
