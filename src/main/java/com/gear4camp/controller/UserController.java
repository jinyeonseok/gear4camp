package com.gear4camp.controller;

import com.gear4camp.domain.User;
import com.gear4camp.dto.user.UserResponseDto;
import com.gear4camp.dto.user.UserUpdateRequestDto;
import com.gear4camp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 가입 API
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return "회원 가입 성공!";
    }

    // 회원 조회 API (userId 기준 조회)
    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") String userId) {
        return userService.getByUserId(userId);
    }

    /*
    // 회원 정보 수정 API
    @PutMapping("/update")
    public String updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return "회원 정보 수정 완료!";
    }
    */

    // 회원 정보 삭제 API
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return "회원 삭제 완료!";
    }

    // JWT 인증된 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(Authentication authentication) {
        String userId = authentication.getName(); // JwtAuthenticationFilter에서 설정한 userId
        User user = userService.getByUserId(userId);
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    // JWT 인증된 사용자 정보 수정
    @PutMapping("/me")
    public ResponseEntity<String> updateMyInfo(@RequestBody UserUpdateRequestDto requestDto,
                                               Authentication authentication) {
        String userId = authentication.getName(); // 토큰에서 추출됨
        userService.updateUser(userId, requestDto);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    // JWT 인증된 사용자 정보 삭제
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
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

}