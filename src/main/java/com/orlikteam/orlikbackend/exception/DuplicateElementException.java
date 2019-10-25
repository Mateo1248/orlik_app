package com.orlikteam.orlikbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateElementException extends RuntimeException {
    public DuplicateElementException(String message) {
        super(message);
    }
}
