package com.test.challenge.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

//customized exceptions
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiCustomBadRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiCustomBadRequestException e){
        ApiException apiException = new ApiException(e.getMessage(), e, HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
        return ResponseEntity.badRequest().body(apiException);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleApiRequestExceptionNotFound(){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
