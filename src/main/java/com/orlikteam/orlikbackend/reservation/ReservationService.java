package com.orlikteam.orlikbackend.reservation;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;


    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Transactional
    public void removeReservation(Integer reservationId) {
        Optional<Reservation> tmpReservation = reservationRepository.findById(reservationId);
        if(tmpReservation.isPresent())
            reservationRepository.deleteById(reservationId);
        else
            throw new ReservationNotFoundException();
    }

    @Transactional
    public Reservation getReservationById(Integer reservationId) {
        return reservationRepository.findById(reservationId).get();
    }

    @Transactional
    public List<Reservation> getReservationByUser(String whichUser) {
        return reservationRepository.findByWhichUser(whichUser);
    }

    @Transactional
    public List<Reservation> getReservationByPitch(Integer whichPitch) {
        return reservationRepository.findByWhichPitch(whichPitch);
    }
}
