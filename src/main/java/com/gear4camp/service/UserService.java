package com.gear4camp.service;

import com.gear4camp.domain.User;
import com.gear4camp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public void registerUser(User user) {
        userMapper.insertUser(user);
    }

    public User getUserByUserId(String userId) {
        return userMapper.findByUserId(userId);
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