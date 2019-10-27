package com.orlikteam.orlikbackend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public void handleUserNotFoundException(UserNotFoundException e) {}

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({UserBadMailException.class})
    public void handleUserBadMailException(UserBadMailException e) {}

    @ResponseStatus(HttpStatus.CREATED)
    @ExceptionHandler({UserAlreadyInDBException.class})
    public void handleUserAlreadyInDBException(UserAlreadyInDBException e) {}

}
