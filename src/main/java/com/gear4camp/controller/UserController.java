package com.gear4camp.controller;

import com.gear4camp.domain.User;
import com.gear4camp.service.UserService;
import lombok.RequiredArgsConstructor;
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
        return userService.getUserByUserId(userId);
    }

    // 회원 정보 수정 API
    @PutMapping("/update")
    public String updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return "회원 정보 수정 완료!";
    }

    // 회원 정보 삭제 API
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return "회원 삭제 완료!";
    }
}