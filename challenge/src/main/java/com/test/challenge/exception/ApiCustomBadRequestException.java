package com.test.challenge.exception;

public class ApiCustomBadRequestException extends RuntimeException{
    public ApiCustomBadRequestException(String message) {
        super(message);
    }

    public ApiCustomBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
