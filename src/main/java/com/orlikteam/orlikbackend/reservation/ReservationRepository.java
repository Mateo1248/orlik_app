package com.orlikteam.orlikbackend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<List<Reservation>> findByPitchIdAndReservationDate(Integer pitchId, Instant reservationDate);
    //Optional<Reservation> findByPitchIdAndReservationDateAnAndStartHourAndEndHour(Integer pitchId, Instant reservationDate, Instant startHour, Instant endHour); it return the same what alternative method below ?

    @Query("SELECT reservation_id, which_user, which_pitch, reservation_date, start_hour, end_hour FROM reservations WHERE (which_pitch = ?1) AND (reservation_date =?2) AND ((start_hour BETWEEN ?3 AND ?4) OR (end_hour BETWEEN ?3 AND ?4))")
    Optional<Reservation> alternativeFindByPitchIdAndReservationDateAnAndStartHourAndEndHour(Integer pitchId, Instant reservationDate, Instant startHour, Instant endHour);
}
