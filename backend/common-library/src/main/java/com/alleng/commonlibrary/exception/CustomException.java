package com.alleng.commonlibrary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String message;
    private final String errorCode;

    public CustomException(String errorCode,String message) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
}
