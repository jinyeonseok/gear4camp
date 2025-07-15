package com.gear4camp.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private Long id;         // 자동 증가 기본키
    private String userId;   // 로그인 ID (유니크)
    private String name;     // 실제 이름 (중복 가능)
    private String password; // 비밀번호
    private String email;    // 이메일 (유니크)
    private LocalDateTime createdAt; // 생성 날짜
    private LocalDateTime updatedAt; // 수정 날짜 (마지막 변경 시점)
}