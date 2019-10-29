package com.orlikteam.orlikbackend.reservation;

import com.orlikteam.orlikbackend.pitch.Pitch;
import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException;
import com.orlikteam.orlikbackend.pitch.PitchRepository;
import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException;
import com.orlikteam.orlikbackend.user.User;
import com.orlikteam.orlikbackend.user.UserRepository;
import com.orlikteam.orlikbackend.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PitchRepository pitchRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, PitchRepository pitchRepository) {
        this.reservationRepository=reservationRepository;
        this.userRepository = userRepository;
        this.pitchRepository = pitchRepository;
    }

    @Transactional
    public Reservation addReservation(ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getWhichUser())
                .orElseThrow(UserNotFoundException::new);
        Pitch pitch = pitchRepository.findById(reservationDto.getWhichPitch())
                .orElseThrow(PitchNotFoundException::new);
        Reservation reservation = getBuiltReservation(reservationDto, user, pitch);
        checkReservationConflict(reservation);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public List<ReservationDto> getReservationByPitchIdAndDate(Integer whichPitch, LocalDate reservationDate) {
        List<Reservation> reservations = reservationRepository.findAllByWhichPitchAndReservationDate(whichPitch, reservationDate);
        return reservations.stream().map(this::getReservationDto).collect(Collectors.toList());
    }

    private void checkReservationConflict(Reservation reservation) {
        if (startHourIsInConflictWithOtherReservation(reservation) || endHourIsInConflictWithOtherReservation(reservation) || reservationHoursAreWithinOtherReservation(reservation)) {
            throw new ReservationAlreadyExistsException();
        }
    }

    private boolean startHourIsInConflictWithOtherReservation(Reservation reservation) {
        return !reservationRepository.findAllByWhichPitchAndReservationDateIsAndStartHourBetween(
                reservation.getWhichPitch().getPitchId(),
                reservation.getReservationDate(),
                reservation.getStartHour(),
                reservation.getEndHour())
                .isEmpty();
    }

    private boolean endHourIsInConflictWithOtherReservation(Reservation reservation) {
        return !reservationRepository.findAllByWhichPitchAndReservationDateIsAndEndHourBetween(
                reservation.getWhichPitch().getPitchId(),
                reservation.getReservationDate(),
                reservation.getStartHour(),
                reservation.getEndHour())
                .isEmpty();
    }

    private boolean reservationHoursAreWithinOtherReservation(Reservation reservation) {
        return !reservationRepository.findAllByWhichPitchAndReservationDateIsAndStartHourBeforeAndEndHourAfter(
                reservation.getWhichPitch().getPitchId(),
                reservation.getReservationDate(),
                reservation.getStartHour(),
                reservation.getEndHour())
                .isEmpty();
    }

    private Reservation getBuiltReservation(ReservationDto reservationDto, User user, Pitch pitch) {
        return Reservation
                .builder()
                .reservationDate(reservationDto.getReservationDate())
                .startHour(reservationDto.getStartHour())
                .endHour(reservationDto.getEndHour())
                .whichUser(user)
                .whichPitch(pitch)
                .build();
    }

    private ReservationDto getReservationDto(Reservation reservation) {
        return ReservationDto
                .builder()
                .reservationDate(reservation.getReservationDate())
                .startHour(reservation.getStartHour())
                .endHour(reservation.getEndHour())
                .whichPitch(reservation.getWhichPitch().getPitchId())
                .whichUser(reservation.getWhichUser().getUserLogin())
                .build();
    }
}
