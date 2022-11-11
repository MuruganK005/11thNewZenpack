package com.ZenPack.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public  class ZenPackException extends Exception{
    private HttpStatus status;
    private String errorMessage;
    public ZenPackException(HttpStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.errorMessage = errorMessage;
    }
}