package com.gear4camp.controller;

import com.gear4camp.dto.auth.LoginRequestDto;
import com.gear4camp.service.AuthService;
import com.gear4camp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "로그인", description = "Login 관련 API")
public class AuthController {

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @Operation(
            summary = "로그인",
            description = "userId와 password를 입력하면 accessToken(JWT)을 반환합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

        String token = authService.login(loginRequestDto);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
