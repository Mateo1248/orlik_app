package com.orlikteam.orlikbackend.reservation;

import com.orlikteam.orlikbackend.pitch.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "SELECT * FROM reservations WHERE which_pitch=?1 AND reservation_date=?2", nativeQuery = true)
    List<Reservation> findAllByWhichPitchAndReservationDate(Integer whichPitch, LocalDate reservationDate);

    @Query(value = "SELECT * FROM reservations WHERE which_pitch=?1 AND reservation_date=?2 AND start_hour < ?3 AND end_hour > ?4", nativeQuery = true)
    List<Reservation> findAllByWhichPitchAndReservationDateIsAndStartHourBeforeAndEndHourAfter(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);

    @Query(value = "SELECT * FROM reservations WHERE which_pitch=?1 AND reservation_date=?2 AND start_hour BETWEEN ?3 AND ?4", nativeQuery = true)
    List<Reservation> findAllByWhichPitchAndReservationDateIsAndStartHourBetween(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);

    @Query(value = "SELECT * FROM reservations WHERE which_pitch=?1 AND reservation_date=?2 AND end_hour BETWEEN ?3 AND ?4", nativeQuery = true)
    List<Reservation> findAllByWhichPitchAndReservationDateIsAndEndHourBetween(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);

    @Query(value = "SELECT * FROM reservations WHERE which_user=?1", nativeQuery = true)
    List<Reservation> findAllByWhichUser(String whichUser);
}