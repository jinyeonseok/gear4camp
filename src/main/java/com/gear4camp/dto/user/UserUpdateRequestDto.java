package com.gear4camp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "사용자 정보 수정 요청 DTO")
public class UserUpdateRequestDto {
    @Schema(description = "사용자 이름", example = "진연석")
    private String name;

    @Schema(description = "이메일 주소", example = "jin@example.com")
    private String email;
}