package com.orlikteam.orlikbackend.pitch.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PitchNotFoundException extends RuntimeException {
    public PitchNotFoundException(String message) {
        super(message);
    }
}
