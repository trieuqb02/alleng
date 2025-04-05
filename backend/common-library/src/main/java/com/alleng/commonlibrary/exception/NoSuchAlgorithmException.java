package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.constant.ErrorCode;

public class NoSuchAlgorithmException extends CustomException {
    public NoSuchAlgorithmException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
