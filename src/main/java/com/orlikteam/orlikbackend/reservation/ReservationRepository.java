package com.orlikteam.orlikbackend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Optional<List<Reservation>> findByWhichPitchAndReservationDate(Integer whichPitch, LocalDate reservationDate);
    Optional<List<Reservation>> findByWhichPitchIsAndReservationDateIsAndStartHourBeforeAndEndHourAfter(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);
    Optional<List<Reservation>> findByWhichPitchIsAndReservationDateIsAndStartHourBetween(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);
    Optional<List<Reservation>> findByWhichPitchIsAndReservationDateIsAndEndHourBetween(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);

}