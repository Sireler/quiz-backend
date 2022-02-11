package com.sireler.quiz.exception;

import com.sireler.quiz.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> resolveException() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("An internal server error occurred.");
        apiResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
