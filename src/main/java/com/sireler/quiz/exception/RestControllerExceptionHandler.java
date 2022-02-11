package com.sireler.quiz.exception;

import com.sireler.quiz.dto.ApiResponse;
import com.sireler.quiz.security.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> resolveException(ApiException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(e.getHttpStatus().value());
        apiResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(apiResponse, e.getHttpStatus());
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ApiResponse> resolveException(JwtAuthenticationException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.UNAUTHORIZED.value());
        apiResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> resolveException() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("An internal server error occurred.");
        apiResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
