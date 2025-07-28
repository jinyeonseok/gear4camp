package com.gear4camp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // 응답본문(body)에 errorCode json필드가 있어야 하는데 컨트롤러는 CustomException이 발생해도 응답 본문을 만들지 않음
    // 해당 코드로 인해 테스트 시 예외 응답을 json 형식으로 받을 수 있고 테스트에도 용이함
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException e) {

        Map<String, String> errorBody = new HashMap<>();

        errorBody.put("errorCode", e.getErrorCode().name());
        errorBody.put("message", e.getErrorCode().getMessage());

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorBody);

    }

}