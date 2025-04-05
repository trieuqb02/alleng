package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.constant.ErrorCode;

public class BadCredentialsException extends CustomException{
    public BadCredentialsException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
