package com.orlikteam.orlikbackend.reservation;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(ReservationService reservationService) {
        this.reservationService=reservationService;
    }

    @PostMapping
    public Integer addReservation(Reservation reservation) {
        return reservationService.addReservation(reservation).getReservationId();
    }

    @GetMapping
    public List<Reservation> getReservationsByPitchIdAndReservationDate(@RequestParam(value = "whichPitch") Integer whichPitch, @RequestParam(value = "reservationDate") LocalDate reservationDate) {
        return reservationService.getReservationByPitchIdAndDate(whichPitch, reservationDate);
    }

}
