package com.orlikteam.orlikbackend.reservation;

import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;


@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository=reservationRepository;
    }

    @Transactional
    public Reservation addReservation(Reservation reservation) {
        if (startHourIsInConflictWithOtherReservation(reservation) || endHourIsInConflictWithOtherReservation(reservation) || reservationHoursAreWithinOtherReservation(reservation)) {
            throw new ReservationAlreadyExistsException();
        }
        return reservationRepository.save(reservation);
    }

    @Transactional
    public List<Reservation> getReservationByPitchIdAndDate(Integer whichPitch, LocalDate reservationDate) {
        return reservationRepository.findAllByWhichPitchAndReservationDate(whichPitch, reservationDate);
    }

    private boolean startHourIsInConflictWithOtherReservation(Reservation reservation) {
        return !reservationRepository.findAllByWhichPitchIsAndReservationDateIsAndStartHourBetween(
                reservation.getWhichPitch(),
                reservation.getReservationDate(),
                reservation.getStartHour(),
                reservation.getEndHour())
                .isEmpty();
    }

    private boolean endHourIsInConflictWithOtherReservation(Reservation reservation) {
        return !reservationRepository.findAllByWhichPitchIsAndReservationDateIsAndEndHourBetween(
                reservation.getWhichPitch(),
                reservation.getReservationDate(),
                reservation.getStartHour(),
                reservation.getEndHour())
                .isEmpty();
    }

    private boolean reservationHoursAreWithinOtherReservation(Reservation reservation) {
        return !reservationRepository.findAllByWhichPitchIsAndReservationDateIsAndStartHourBeforeAndEndHourAfter(
                reservation.getWhichPitch(),
                reservation.getReservationDate(),
                reservation.getStartHour(),
                reservation.getEndHour())
                .isEmpty();
    }
}
