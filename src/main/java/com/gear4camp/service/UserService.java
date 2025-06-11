package com.gear4camp.service;

import com.gear4camp.domain.User;
import com.gear4camp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        userMapper.insertUser(user);
    }

    public Optional<User> getUserByUserId(String userId) {
        return Optional.ofNullable(userMapper.findByUserId(userId));
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 userId가 존재하지 않습니다: " + userId);
        }
        userMapper.deleteUser(userId);
    }
}