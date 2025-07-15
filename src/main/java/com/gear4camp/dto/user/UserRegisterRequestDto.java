package com.gear4camp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원가입 요청 DTO")
public class UserRegisterRequestDto {

    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Schema(description = "사용자 ID", example = "jinyeon123")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "비밀번호", example = "securePassword123!")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @Schema(description = "이메일 주소", example = "jin@example.com")
    private String email;

}