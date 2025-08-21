package com.gear4camp.dto.user;

import com.gear4camp.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자 정보 응답 DTO")
public class UserResponseDto {

    @Schema(description = "로그인 ID", example = "jinyeon123")
    private final String userId;

    @Schema(description = "사용자 이름", example = "진연석")
    private final String name;

    @Schema(description = "이메일 주소", example = "jin@example.com")
    private final String email;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}