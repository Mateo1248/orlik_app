package com.orlikteam.orlikbackend.reservation.unit

import com.orlikteam.orlikbackend.reservation.Reservation
import com.orlikteam.orlikbackend.reservation.ReservationResource
import com.orlikteam.orlikbackend.reservation.exception.ReservationAlreadyExistsException
import com.orlikteam.orlikbackend.reservation.exception.ReservationNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest
class ReservationResourceSpec extends Specification {

    @Autowired
    ReservationResource reservationResource

    def "should add reservation" () {
        given:
        def reservation = getReservation(1, "root@gmail.com", 5, LocalDate.of(2019, 12, 12), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))

        when:
        def createdReservationId = reservationResource.addReservation(reservation)

        then:
        createdReservationId == 1
    }

    def "should throw exception due to already existing reservation in the same time in db"() {
        given:
        def reservation = getReservation(2, "root@gmail.com", 5, LocalDate.of(2019, 11, 12), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        reservationResource.addReservation(reservation)

        when:
        reservationResource.addReservation(reservation)

        then:
        thrown(ReservationAlreadyExistsException)
    }

    /*def "should throw exception due to already existing reservation in covering time in db"() {
        given:
        def reservation = getReservation(2, "root@gmail.com", 5, LocalDate.of(2019, 11, 12), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        def conflictedReservation = getReservation(3, "root@gmail.com", 5, LocalDate.of(2019, 11, 12), LocalTime.of(13, 00, 00), LocalTime.of(14, 30, 00))
        reservationResource.addReservation(reservation)

        when:
        reservationResource.addReservation(conflictedReservation)

        then:
        thrown(ReservationAlreadyExistsException)
    }*/

    def "should get all reservations from one day"() {
        given:
        def reservation1 = getReservation(20, "root@gmail.com", 5, LocalDate.of(2020, 01, 01), LocalTime.of(11, 00, 00), LocalTime.of(13, 30, 00))
        def reservation2 = getReservation(30, "killer@gmail.com", 5, LocalDate.of(2020, 01, 01), LocalTime.of(17, 00, 00), LocalTime.of(19, 15, 00))
        def reservation3 = getReservation(40, "bulbazaur@gmail.com", 4, LocalDate.of(2020, 01, 01), LocalTime.of(17, 00, 00), LocalTime.of(19, 15, 00))
        def reservation4 = getReservation(50, "partymaker@gmail.com", 5, LocalDate.of(2020, 01, 02), LocalTime.of(17, 00, 00), LocalTime.of(19, 15, 00))
        def reservation5 = getReservation(60, "qwertt@gmail.com", 7, LocalDate.of(2020, 01, 05), LocalTime.of(15, 00, 00), LocalTime.of(16, 15, 00))
        def reservation6 = getReservation(70, "zzzzz@gmail.com", 5, LocalDate.of(2020, 01, 01), LocalTime.of(8, 10, 00), LocalTime.of(10, 35, 00))
        List<Reservation> sampleReservations = new ArrayList<>()
        sampleReservations.add(reservation1)
        sampleReservations.add(reservation2)
        sampleReservations.add(reservation3)
        sampleReservations.add(reservation4)
        sampleReservations.add(reservation5)
        sampleReservations.add(reservation6)
        for(Reservation reservation : sampleReservations) {
            reservationResource.addReservation(reservation)
        }

        when:
        def listOfReservations = reservationResource.getReservationByPitchIdAndReservationDate(5, LocalDate.of(2020, 01, 01))

        then:
        listOfReservations.size()==3

    }

    def "should throw exception due to attempt of getting reservations from free of reservations day"() {
        when:
        reservationResource.getReservationByPitchIdAndReservationDate(4, LocalDate.of(2019, 11, 13))

        then:
        thrown(ReservationNotFoundException)
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
