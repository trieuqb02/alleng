package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.constant.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
