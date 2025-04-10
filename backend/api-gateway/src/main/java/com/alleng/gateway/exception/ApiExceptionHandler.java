package com.alleng.gateway.exception;

import com.alleng.gateway.payload.response.ErrorVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorVM> handleException(CustomException ex) {
        return buildErrorResponse(ex);
    }

    private ResponseEntity<ErrorVM> buildErrorResponse(CustomException ex) {
        ErrorVM errorV = new ErrorVM(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(ex.getStatus())).body(errorV);
    }
}
