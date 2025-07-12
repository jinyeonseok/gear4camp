package com.gear4camp.controller;

import com.gear4camp.dto.auth.LoginRequestDto;
import com.gear4camp.service.AuthService;
import com.gear4camp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(
            summary = "로그인",
            description = "userId와 password를 입력하면 accessToken(JWT)을 반환합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
