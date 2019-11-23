package com.orlikteam.orlikbackend.reservation;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(ReservationService reservationService) {
        this.reservationService=reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationIdDto addReservation(@RequestBody @Validated ReservationDto reservation) {
        return reservationService.addReservation(reservation);
    }

    @GetMapping("/{whichPitch}/{reservationDate}")
    public List<ReservationDto> getReservationsByPitchIdAndReservationDate(@PathVariable("whichPitch") Integer whichPitch,
                                                                           @PathVariable("reservationDate") @DateTimeFormat(iso = DATE) LocalDate reservationDate) {
        return reservationService.getReservationByPitchIdAndDate(whichPitch, reservationDate);
    }


    @GetMapping("/{whichUser}")
    public List<ReservationDto> getReservationByUserLogin(@PathVariable("whichUser") String whichUser) {
        return reservationService.getReservationByWhichUser(whichUser);
    }

    @DeleteMapping(value = "/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable(value = "reservationId") int reservationId) {
        reservationService.cancelReservation(reservationId);
    }
}
