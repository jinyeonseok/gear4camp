package com.gear4camp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDto {
    @Schema(description = "사용자 ID", example = "testuser")
    private String userId;

    @Schema(description = "비밀번호", example = "password123")
    private String password;
}