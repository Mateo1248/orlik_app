package com.orlikteam.orlikbackend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findAllByWhichPitchAndReservationDate(Integer whichPitch, LocalDate reservationDate);
    List<Reservation> findAllByWhichPitchIsAndReservationDateIsAndStartHourBeforeAndEndHourAfter(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);
    List<Reservation> findAllByWhichPitchIsAndReservationDateIsAndStartHourBetween(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);
    List<Reservation> findAllByWhichPitchIsAndReservationDateIsAndEndHourBetween(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);

}