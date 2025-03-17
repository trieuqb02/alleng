package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.payload.ErrorVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorVM> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorVM> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorVM> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex);
    }

    private ResponseEntity<ErrorVM> buildErrorResponse(HttpStatus status, CustomException ex){
        ErrorVM errorV = new ErrorVM(ex.getErrorCode(), ex.getMessage(), ex.getErrors());
        return ResponseEntity.status(status).body(errorV);
    }
}
