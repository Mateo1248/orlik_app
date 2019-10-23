package com.orlikteam.orlikbackend.reservation;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
        reservationRepository.deleteById(reservationId);
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
