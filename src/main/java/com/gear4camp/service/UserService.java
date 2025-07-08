package com.gear4camp.service;

import com.gear4camp.domain.User;
import com.gear4camp.dto.user.UserUpdateRequestDto;
import com.gear4camp.exception.CustomException;
import com.gear4camp.exception.ErrorCode;
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

    public User getByUserId(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public void updateUser(String userId, UserUpdateRequestDto dto) {
        User user = getByUserId(userId);

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

    public Long getUserDbId(String userId) {
        return getByUserId(userId).getId();
    }
}