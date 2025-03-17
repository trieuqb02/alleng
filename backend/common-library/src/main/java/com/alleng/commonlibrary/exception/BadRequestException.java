package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.payload.ErrorVM.Error;

import java.util.ArrayList;
import java.util.List;

public class BadRequestException extends CustomException{
    public BadRequestException(String errorCode,String message) {
        super(errorCode,message, new ArrayList<>());
    }

    public BadRequestException(String errorCode,String message, List<Error> errors) {
        super(errorCode,message, errors);
    }
}
