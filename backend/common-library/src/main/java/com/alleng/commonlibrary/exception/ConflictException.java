package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.constant.ErrorCode;

public class ConflictException extends CustomException {

    public ConflictException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
