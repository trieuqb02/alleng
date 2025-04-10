package com.alleng.commonlibrary.exception;

import com.alleng.commonlibrary.payload.ErrorVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorVM> handleFeignException(FeignException ex, WebRequest request) {
        ErrorVM errorV = new ErrorVM(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(ex.getStatus())).body(errorV);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorVM> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorVM> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorVM> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorVM> handleAccessDeniedException(ConflictException ex, WebRequest request) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorVM> handleTokenException(TokenException ex, WebRequest request) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorVM> handleAccessDeniedException(BadCredentialsException ex, WebRequest request) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorVM> handleAccessDeniedException(InternalServerErrorException ex, WebRequest request) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException authException, WebRequest request) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("timestamp", new Date());
        responseData.put("message", authException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorVM> handleOAuth2Exception(OAuth2AuthenticationException ex) {
        OAuth2Error oAuth2Error = ex.getError();
        ErrorVM errorV = new ErrorVM(oAuth2Error.getErrorCode(), oAuth2Error.getDescription());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorV);
    }

    private ResponseEntity<ErrorVM> buildErrorResponse(CustomException ex) {
        ErrorVM errorV = new ErrorVM(ex.getErrorCode().getCode(), ex.getErrorMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorV);
    }
}
