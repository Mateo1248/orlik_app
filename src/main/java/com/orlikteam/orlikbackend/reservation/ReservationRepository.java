package com.orlikteam.orlikbackend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<List<Reservation>> findByWhichPitchAndReservationDate(Integer whichPitch, LocalDate reservationDate);
    Optional<Reservation> findByWhichPitchAndReservationDateAndStartHourAndEndHour(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);
    //only in case of ideal the same hours, in mid of other reservation, it does not work

    //@Query("SELECT reservation_id, which_user, which_pitch, reservation_date, start_hour, end_hour FROM reservations WHERE (which_pitch = ?1) AND (reservation_date =?2) AND ((start_hour BETWEEN ?3 AND ?4) OR (end_hour BETWEEN ?3 AND ?4))")
    //Optional<Reservation> alternativeFindByPitchIdAndReservationDateAndStartHourAndEndHour(Integer whichPitch, LocalDate reservationDate, LocalTime startHour, LocalTime endHour);
    //mapping is needed but method will be accurate/without errors in each case
}
