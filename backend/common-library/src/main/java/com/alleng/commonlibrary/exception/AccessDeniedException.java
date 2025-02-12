package com.alleng.commonlibrary.exception;

public class AccessDeniedException extends CustomException{
    public AccessDeniedException(String errorCode, String message) {
        super(errorCode,message);
    }
}
