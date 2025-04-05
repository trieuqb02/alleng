package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.constant.ErrorCode;

public class InternalServerErrorException extends CustomException{
    public InternalServerErrorException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
