package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.constant.ErrorCode;

public class AccessDeniedException extends CustomException {
    public AccessDeniedException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
