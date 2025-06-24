package com.gear4camp.dto.user;

import com.gear4camp.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String userId;
    private final String name;
    private final String email;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}