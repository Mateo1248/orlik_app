package com.orlikteam.orlikbackend.reservation;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(ReservationService reservationService) {
        this.reservationService=reservationService;
    }

    @PostMapping
    public void addReservation(Reservation reservation) {
        reservationService.addReservation(reservation);
    }

    @GetMapping("/{pitchId}/{reservationDate}")
    public List<Reservation> getReservationByPitchIdAndReservationDate(@PathVariable Integer pitchId, @PathVariable Instant reservationDate) {
        return reservationService.getReservationByPitchIdAndDate(pitchId, reservationDate);
    }

}
