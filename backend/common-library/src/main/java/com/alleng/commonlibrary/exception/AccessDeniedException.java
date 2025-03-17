package com.alleng.commonlibrary.exception;

import java.util.ArrayList;

public class AccessDeniedException extends CustomException{
    public AccessDeniedException(String errorCode, String message) {
        super(errorCode,message, new ArrayList<>());
    }
}
