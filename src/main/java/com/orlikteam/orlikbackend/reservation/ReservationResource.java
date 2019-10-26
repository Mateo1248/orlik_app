package com.orlikteam.orlikbackend.reservation;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public void addReservation(Reservation reservation) {
        reservationService.addReservation(reservation);
    }

    @DeleteMapping("/{reservationId}")
    public void removeReservation(@PathVariable Integer reservationId) {
        reservationService.removeReservation(reservationId);
    }

    @GetMapping("/{whichUser}")
    public List<Reservation> getReservationByUser(@PathVariable String whichUser) {
        return reservationService.getReservationByUser(whichUser);
    }

    @GetMapping("/{whichPitch}")
    public List<Reservation> getReservationByPitch(@PathVariable Integer whichPitch) {
        return reservationService.getReservationByPitch(whichPitch);
    }

    @GetMapping("/{whichId}")
    public Reservation getReservationById(@PathVariable Integer reservationId) {
        return reservationService.getReservationById(reservationId);
    }


}
