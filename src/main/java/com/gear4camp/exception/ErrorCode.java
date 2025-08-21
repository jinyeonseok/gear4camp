package com.gear4camp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /*
    * NOT_FOUND : 404(리소스를 찾을 수 없음)
    * CONFLICT : 409(중복된 값이 들어올 때)
    * UNAUTHORIZED : 401(로그인등의 인증 실패)
    * FORBIDDEN : 403(인증은 되었지만 권한이 없을 때)
    * BAD_REQUEST : 400(요청 자체가 잘못된 경우)
    */

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "이미 존재하는 사용자 ID입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 상품 관련
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

    // 권한 관련
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NO_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    // 장바구니 관련
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 항목을 찾을 수 없습니다."),

    // 주문 관련
    ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}