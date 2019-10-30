package com.orlikteam.orlikbackend.reservation.unit

import com.orlikteam.orlikbackend.reservation.Reservation
import com.orlikteam.orlikbackend.reservation.ReservationRepository
import com.orlikteam.orlikbackend.reservation.ReservationService
import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

class ReservationServiceSpec extends Specification {

    private ReservationService reservationService
    private ReservationRepository reservationRepository

    def setup() {
        reservationRepository = Mock(ReservationRepository)
        reservationService = new ReservationService(reservationRepository, userRepository, pitchRepository)
    }

    def "should add reservation" () {
        given:
        def reservation = getReservation(1, "root@gmail.com", 5, LocalDate.of(2019, 12, 20), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        reservationRepository.save(reservation) >> reservation
        reservationRepository.findAllByWhichPitchAndReservationDateIsAndStartHourBeforeAndEndHourAfter(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour()) >> Collections.emptyList()
        reservationRepository.findAllByWhichPitchAndReservationDateIsAndEndHourBetween(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour()) >> Collections.emptyList()
        reservationRepository.findAllByWhichPitchAndReservationDateIsAndStartHourBetween(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour()) >> Collections.emptyList()

        when:
        def createdReservation = reservationService.addReservation(reservation)

        then:
        with (createdReservation) {
            reservationId==1
            whichUser=="root@gmail.com"
            whichPitch==5
            reservationDate==LocalDate.of(2019, 12, 20)
            startHour==LocalTime.of(11, 00, 00)
            endHour==LocalTime.of(13, 30, 00)
        }
    }

    def "should throw exception due to already existing reservation in the same time in db"() {
        given:
        def reservation = getReservation(1, "root@gmail.com", 5, LocalDate.of(2019, 12, 20), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        reservationRepository.save(reservation) >> reservation
        reservationRepository.findAllByWhichPitchAndReservationDateIsAndStartHourBeforeAndEndHourAfter(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour()) >> List.of(reservation)
        reservationRepository.findAllByWhichPitchAndReservationDateIsAndStartHourBetween(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour()) >> List.of(reservation)
        reservationRepository.findAllByWhichPitchAndReservationDateIsAndEndHourBetween(reservation.getWhichPitch(), reservation.getReservationDate(), reservation.getStartHour(), reservation.getEndHour()) >> List.of(reservation)

        when:
        reservationService.addReservation(reservation)

        then:
        thrown(ReservationAlreadyExistsException)
    }

    def "should throw exception due to attempt of getting reservations from free of reservations day"() {
        given:
        reservationRepository.findAllByWhichPitchAndReservationDate(5, LocalDate.of(2019, 10, 30)) >> Collections.emptyList()

        when:
        def listOfReservations = reservationService.getReservationByPitchIdAndDate(5, LocalDate.of(2019, 10, 30))

        then:
        listOfReservations.size()==0
    }

    def "should get all reservations from one day"() {
        given:
        def reservation1 = getReservation(20, "root@gmail.com", 5, LocalDate.of(2020, 01, 01), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        def reservation2 = getReservation(30, "killer@gmail.com", 5, LocalDate.of(2020, 01, 01), LocalTime.of(17, 00, 00), LocalTime.of(19, 15, 00))
        def reservation3 = getReservation(70, "zzzzz@gmail.com", 5, LocalDate.of(2020, 01, 01), LocalTime.of(8, 10, 00), LocalTime.of(10, 35, 00))
        def reservation4 = getReservation(73, "zzzzz@gmail.com", 5, LocalDate.of(2020, 01, 01), LocalTime.of(14,20, 00), LocalTime.of(16, 35, 00))
        List<Reservation> sampleReservations = new ArrayList<>()
        sampleReservations.add(reservation1)
        sampleReservations.add(reservation2)
        sampleReservations.add(reservation3)
        sampleReservations.add(reservation4)
        reservationRepository.findAllByWhichPitchAndReservationDate(5, LocalDate.of(2020, 01, 01)) >> sampleReservations

        when:
        def takenReservationList = reservationService.getReservationByPitchIdAndDate(5, LocalDate.of(2020, 01, 01))

        then:
        takenReservationList.size()==4

    }

    private static Reservation getReservation(Integer id, String user, Integer pitch, LocalDate date, LocalTime startHour, LocalTime endHour) {
        return Reservation
                .builder()
                .reservationId(id)
                .whichUser(user)
                .whichPitch(pitch)
                .reservationDate(date)
                .startHour(startHour)
                .endHour(endHour)
                .build()
    }

}
