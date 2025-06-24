package com.gear4camp.service;

import com.gear4camp.domain.User;
import com.gear4camp.dto.user.UserUpdateRequestDto;
import com.gear4camp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void updateUser(String userId, UserUpdateRequestDto dto) {
        User user = getUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

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