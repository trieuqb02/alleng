package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.constant.ErrorCode;

public class TokenException extends CustomException{
    public TokenException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
