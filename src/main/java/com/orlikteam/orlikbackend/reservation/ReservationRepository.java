package com.orlikteam.orlikbackend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByWhichUser(String whichUser);
    List<Reservation> findByWhichPitch(Integer whichPitch);
    List<Reservation> findByDate(Instant reservationDate);
}
