package com.orlikteam.orlikbackend.reservation;

import org.springframework.format.annotation.DateTimeFormat;
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
    public Integer addReservation(@RequestBody ReservationDto reservation) {
        return reservationService.addReservation(reservation).getReservationId();
    }

    @GetMapping
    public List<ReservationDto> getReservationsByPitchIdAndReservationDate(@RequestParam(value = "whichPitch") Integer whichPitch,
                                                                        @RequestParam(value = "reservationDate") @DateTimeFormat(iso = DATE) LocalDate reservationDate) {
        return reservationService.getReservationByPitchIdAndDate(whichPitch, reservationDate);
    }

}
