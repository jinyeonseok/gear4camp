package com.gear4camp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice // 전역 예외 처리기(@ControllerAdvice + @ResponseBody 합친 상태)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // @Valid + DTO validation 어노테이션 실패 시 발생
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()// 검증에 실패한 필드별 에러 정보 목록 들고옴
                .stream()
                .map(fieldError -> new ErrorResponse.ValidationError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Validation failed",
                validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 다른 예외도 추가 가능 (예: CustomException)
}