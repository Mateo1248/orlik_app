package com.orlikteam.orlikbackend.reservation.unit

import com.orlikteam.orlikbackend.pitch.Pitch
import com.orlikteam.orlikbackend.pitch.PitchRepository
import com.orlikteam.orlikbackend.reservation.Reservation
import com.orlikteam.orlikbackend.reservation.ReservationRepository
import com.orlikteam.orlikbackend.reservation.ReservationDto
import com.orlikteam.orlikbackend.reservation.ReservationService
import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException
import com.orlikteam.orlikbackend.user.User
import com.orlikteam.orlikbackend.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest
@DirtiesContext
class ReservationServiceSpec extends Specification {

    private static final String TEST_EMAIL = "root@gmail.com"
    private static final String TEST_PASSWORD = "test"
    private static final int PITCH_ID = 1

    @Autowired
    private ReservationService reservationService
    @Autowired
    private ReservationRepository reservationRepository
    @Autowired
    private UserRepository userRepository
    @Autowired
    private PitchRepository pitchRepository

    def setup() {
        userRepository.save(buildUser(TEST_EMAIL, TEST_PASSWORD))
        pitchRepository.save(buildPitch())
    }

    def "should add reservation"() {
        given:
        def reservation = buildReservationDto(TEST_EMAIL, PITCH_ID, LocalDate.of(2020, 01, 04), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))

        when:
        def createdReservation = reservationService.addReservation(reservation)

        then:
        with(createdReservation) {
            reservationId
        }
    }

    def "should throw exception due to already existing reservation in the same time in db"() {
        given:
        def reservation = buildReservationDto(TEST_EMAIL, PITCH_ID, LocalDate.of(2019, 12, 20), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        reservationService.addReservation(reservation)

        when:
        reservationService.addReservation(reservation)

        then:
        thrown(ReservationAlreadyExistsException)
    }

    def "should throw exception due to attempt of getting reservations from free of reservations day"() {
        when:
        def listOfReservations = reservationService.getReservationByPitchIdAndDate(5, LocalDate.of(2019, 10, 30))

        then:
        listOfReservations.size() == 0
    }

    def "should get all reservations from one day"() {
        given:
        def reservation1 = buildReservationDto(TEST_EMAIL, PITCH_ID, LocalDate.of(2020, 01, 01), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        def reservation2 = buildReservationDto(TEST_EMAIL, PITCH_ID, LocalDate.of(2020, 01, 01), LocalTime.of(17, 00, 00), LocalTime.of(19, 15, 00))
        reservationService.addReservation(reservation1)
        reservationService.addReservation(reservation2)

        when:
        def takenReservationList = reservationService.getReservationByPitchIdAndDate(PITCH_ID, LocalDate.of(2020, 01, 01))

        then:
        takenReservationList.size() == 2
    }

    private static ReservationDto buildReservationDto(String user, Integer pitch, LocalDate date, LocalTime startHour, LocalTime endHour) {
        return ReservationDto
                .builder()
                .whichUser(user)
                .whichPitch(pitch)
                .reservationDate(date)
                .startHour(startHour)
                .endHour(endHour)
                .build()
    }

    private static Reservation buildReservation(ReservationDto reservationRequestDto, int reservationId) {
        return Reservation
                .builder()
                .reservationId(reservationId)
                .whichPitch(buildPitch(reservationRequestDto.whichPitch))
                .whichUser(buildUser(reservationRequestDto.whichUser, TEST_PASSWORD))
                .endHour(reservationRequestDto.endHour)
                .startHour(reservationRequestDto.startHour)
                .reservationDate(reservationRequestDto.reservationDate)
                .build()
    }

    private static User buildUser(String login, String password) {
        return User
                .builder()
                .userLogin(login)
                .userPassword(password)
                .build()
    }

    private static Pitch buildPitch() {
        return Pitch
                .builder()
                .pitchName("Pitch")
                .longitude(50)
                .latitude(50)
                .build()
    }
}
