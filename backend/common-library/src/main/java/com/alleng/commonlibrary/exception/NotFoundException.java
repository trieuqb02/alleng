package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.payload.ErrorVM.Error;

import java.util.ArrayList;
import java.util.List;

public class NotFoundException extends CustomException {

    public NotFoundException(String errorCode,String message) {
        super(errorCode,message, new ArrayList<>());
    }

    public NotFoundException(String errorCode,String message, List<Error> errors) {
        super(errorCode,message, errors);
    }
}
