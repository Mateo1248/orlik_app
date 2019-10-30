package com.orlikteam.orlikbackend.pitch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PitchExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({PitchNotFoundException.class})
    public void handlePitchNotFoundException(PitchNotFoundException e) {}
}
