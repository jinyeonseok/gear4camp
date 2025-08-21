package com.gear4camp.controller;

import com.gear4camp.domain.User;
import com.gear4camp.dto.user.UserRegisterRequestDto;
import com.gear4camp.dto.user.UserResponseDto;
import com.gear4camp.dto.user.UserUpdateRequestDto;
import com.gear4camp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "사용자", description = "User 관련 API")
public class UserController {

    private final UserService userService;

    // 회원 가입 API
    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {

        userService.registerUser(userRegisterRequestDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 가입 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // JWT 인증된 사용자 정보 조회
    @Operation(summary = "내 정보 조회", description = "JWT 인증된 사용자의 정보를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(Authentication authentication) {
        String userId = authentication.getName(); // JwtAuthenticationFilter에서 설정한 userId

        User user = userService.getByUserId(userId);
        UserResponseDto dto = new UserResponseDto(user);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
//        return ResponseEntity.ok(new UserResponseDto(user)); -> 축약형
    }

    // JWT 인증된 사용자 정보 수정
    @Operation(summary = "내 정보 수정", description = "JWT 인증된 사용자의 정보를 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/me")
    public ResponseEntity<String> updateMyInfo(@RequestBody UserUpdateRequestDto requestDto,
                                               Authentication authentication) {
        String userId = authentication.getName(); // 토큰에서 추출됨
        userService.updateUser(userId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("회원정보가 수정되었습니다.");
    }

    // JWT 인증된 사용자 정보 삭제
    @Operation(summary = "내 정보 삭제", description = "JWT 인증된 사용자의 정보를 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyInfo(Authentication authentication) {
        String userId = authentication.getName(); // 토큰에서 추출됨
        try {
            userService.deleteUser(userId);
        } catch (IllegalArgumentException e) {
            // 이미 삭제된 경우(403 방지)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 탈퇴된 사용자입니다.");
        }

        SecurityContextHolder.clearContext(); // 인증 정보 수동 제거 (403 예외 방지)
        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴가 완료되었습니다.");
    }

}