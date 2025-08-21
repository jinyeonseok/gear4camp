package com.gear4camp.service;

import com.gear4camp.domain.User;
import com.gear4camp.dto.user.UserRegisterRequestDto;
import com.gear4camp.dto.user.UserUpdateRequestDto;
import com.gear4camp.exception.CustomException;
import com.gear4camp.exception.ErrorCode;
import com.gear4camp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegisterRequestDto dto) {
        // 중복된 userId 확인
        if(userMapper.findByUserId(dto.getUserId()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setUserId(dto.getUserId());
        user.setName(dto.getName());
        user.setPassword(encodedPassword);
        user.setEmail(dto.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

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