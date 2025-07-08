package com.gear4camp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private List<ValidationError> errors;

    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String reason;
    }
}
