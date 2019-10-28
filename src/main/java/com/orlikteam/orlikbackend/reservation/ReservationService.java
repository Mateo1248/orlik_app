package com.orlikteam.orlikbackend.reservation;

import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException;
import com.orlikteam.orlikbackend.reservation.exception.ReservationNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
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
        Optional<Reservation> maybeReservation = reservationRepository.alternativeFindByPitchIdAndReservationDateAnAndStartHourAndEndHour(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour());
        if(maybeReservation.isPresent())
            throw new ReservationAlreadyExistsException();
        return reservationRepository.save(reservation);
    }

    @Transactional
    public List<Reservation> getReservationByPitchIdAndDate(Integer pitchId, Instant reservationDate) {
        Optional<List<Reservation>> pitchReservations = reservationRepository.findByPitchIdAndReservationDate(pitchId, reservationDate);
        if(pitchReservations.isEmpty())
            throw new ReservationNotFoundException();
        return pitchReservations.get();
    }
}
