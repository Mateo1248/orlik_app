package com.orlikteam.orlikbackend.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ReservationAlreadyExistsException.class})
    public void handleReservationAlreadyExistsException(ReservationAlreadyExistsException e) {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ReservationNotFoundException.class})
    public void handleReservationNotFoundException(ReservationNotFoundException e) {}
}
