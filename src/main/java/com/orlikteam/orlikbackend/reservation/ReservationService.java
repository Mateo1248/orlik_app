package com.orlikteam.orlikbackend.reservation;

import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository=reservationRepository;
    }

    @Transactional
    public Reservation addReservation(Reservation reservation) {
        Optional<List<Reservation>> possibleExistingReservations1 = reservationRepository.findByWhichPitchIsAndReservationDateIsAndStartHourBeforeAndEndHourAfter(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour());
        if(!possibleExistingReservations1.isEmpty())
            throw new ReservationAlreadyExistsException();
        Optional<List<Reservation>> possibleExistingReservations2 = reservationRepository.findByWhichPitchIsAndReservationDateIsAndStartHourBetween(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour());
        if(!possibleExistingReservations2.isEmpty())
            throw new ReservationAlreadyExistsException();
        Optional<List<Reservation>> possibleExistingReservations3 = reservationRepository.findByWhichPitchIsAndReservationDateIsAndEndHourBetween(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour());
        if(!possibleExistingReservations3.isEmpty())
            throw new ReservationAlreadyExistsException();
        return reservationRepository.save(reservation);
    }

    @Transactional
    public List<Reservation> getReservationByPitchIdAndDate(Integer whichPitch, LocalDate reservationDate) {
        if (reservationRepository.findByWhichPitchAndReservationDate(whichPitch, reservationDate).isPresent())
            return reservationRepository.findByWhichPitchAndReservationDate(whichPitch, reservationDate).get();
        return Collections.emptyList();
    }
}
