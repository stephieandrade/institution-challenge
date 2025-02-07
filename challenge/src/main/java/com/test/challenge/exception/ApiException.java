package com.test.challenge.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
@Getter
@Setter
public class ApiException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public ApiException(String message, Throwable throwable, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
