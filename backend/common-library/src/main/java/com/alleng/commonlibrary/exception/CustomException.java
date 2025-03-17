package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.payload.ErrorVM.Error;
import lombok.Getter;

import java.util.List;


@Getter
public class CustomException extends RuntimeException {
    private final String message;
    private final String errorCode;
    private final List<Error> errors;

    public CustomException(String errorCode, String message, List<Error> errors) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
        this.errors = errors;
    }
}
