package com.orlikteam.orlikbackend.reservation.exception;

import com.orlikteam.orlikbackend.user.exception.UserAlreadyExistsException;
import com.orlikteam.orlikbackend.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ReservationNotFoundException.class})
    public void handleReservationNotFoundException(ReservationNotFoundException e) {}

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ReservationAlreadyExistsException.class})
    public void handleReservationAlreadyExistsException(ReservationAlreadyExistsException e) {}
}
