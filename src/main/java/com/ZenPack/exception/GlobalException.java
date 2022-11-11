package com.ZenPack.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException{
    private Logger logger = LoggerFactory.getLogger(GlobalException.class);
    @ExceptionHandler(value = ZenPackException.class)
    public ResponseEntity<?> handleTeamException(ZenPackException exception) {
        logger.error("ZenPack Exception: {}", exception.getErrorMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(exception.getErrorMessage())
                .status(exception.getStatus())
                .build(), exception.getStatus());
    }
}