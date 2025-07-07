package com.gear4camp.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 사용자 관련
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),

    // 상품 관련
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),

    // 권한 관련
    FORBIDDEN("권한이 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}