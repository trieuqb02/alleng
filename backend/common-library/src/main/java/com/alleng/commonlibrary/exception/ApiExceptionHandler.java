package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.payload.ErrorV;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorV> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorV errorV = new ErrorV("400", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorV);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorV> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex);
    }

    private ResponseEntity<ErrorV> buildErrorResponse(HttpStatus status, CustomException ex){
        ErrorV errorV = new ErrorV(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(status).body(errorV);
    }
}
