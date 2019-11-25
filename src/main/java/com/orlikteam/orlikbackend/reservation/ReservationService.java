package com.orlikteam.orlikbackend.reservation;

import com.orlikteam.orlikbackend.pitch.Pitch;
import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException;
import com.orlikteam.orlikbackend.pitch.PitchRepository;
import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException;
import com.orlikteam.orlikbackend.reservation.exception.ReservationNotFoundException;
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
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.pitchRepository = pitchRepository;
    }


    /**
     * method is used to add a new reservation to app
     * @param reservationDto is a object made from: date, start and end hours and chosen pitch given by user
     * @throws @UserNotFoundException
     * @throws @PitchNotFoundException
     * @throws @ReservationAlreadyExistsException
     * @return reservation id as a response of properly added reservation given from repository
     */
    @Transactional
    public ReservationIdDto addReservation(ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getWhichUser())
                .orElseThrow(UserNotFoundException::new);
        Pitch pitch = pitchRepository.findById(reservationDto.getWhichPitch())
                .orElseThrow(PitchNotFoundException::new);
        Reservation reservation = getBuiltReservation(reservationDto, user, pitch);
        checkReservationConflict(reservation);
        return getReservationResponseDtoOf(reservationRepository.save(reservation).getReservationId());
    }


    /**
     * method is used to get existing reservations from app (on selected pitch and date)
     * @param whichPitch is a pitch id from which we are eager to get reservations
     * @param reservationDate  is a chosen date from which we are eager to get reservations
     * @throws @PitchNotFoundException
     * @return list of reservations existing on selected pitch and date as a response from repository
     */
    @Transactional
    public List<ReservationDto> getReservationByPitchIdAndDate(Integer whichPitch, LocalDate reservationDate) {
        var pitch = pitchRepository.findById(whichPitch).orElseThrow(PitchNotFoundException::new);
        List<Reservation> reservations = reservationRepository.findAllByWhichPitchAndReservationDate(pitch.getPitchId(), reservationDate);
        return reservations.stream().map(this::getReservationDto).collect(Collectors.toList());
    }


    /**
     * method is used to get existing reservations from app (for logged user)
     * @param whichUser is a user login for whom we are eager to get reservations
     * @throws @UserNotFoundException
     * @return list of reservations which current user have as a response from repository
     */
    @Transactional
    public List<ReservationDto> getReservationByWhichUser(String whichUser) {
        var user = userRepository.findById(whichUser).orElseThrow(UserNotFoundException::new);
        List<Reservation> userReservations = reservationRepository.findAllByWhichUser(user.getUserLogin());
        return userReservations.stream().map(this::getReservationDto).collect(Collectors.toList());
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
                .reservationId(reservation.getReservationId())
                .reservationDate(reservation.getReservationDate())
                .startHour(reservation.getStartHour())
                .endHour(reservation.getEndHour())
                .whichPitch(reservation.getWhichPitch().getPitchId())
                .whichUser(reservation.getWhichUser().getUserLogin())
                .build();
    }

    private static ReservationIdDto getReservationResponseDtoOf(int reservationId) {
        return ReservationIdDto
                .builder()
                .reservationId(reservationId)
                .build();
    }


    /**
     * method is used to remove an existing reservation from app
     * @param reservationId is a reservation id which we are eager to delete from app
     * @throws @ReservationNotFoundException
     */
    public void cancelReservation(int reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
        reservationRepository.deleteById(reservation.getReservationId());
    }
}
