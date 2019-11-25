package com.orlikteam.orlikbackend.pitch.unit

import com.orlikteam.orlikbackend.pitch.Pitch
import com.orlikteam.orlikbackend.pitch.PitchRepository
import com.orlikteam.orlikbackend.pitch.PitchResponseDto
import com.orlikteam.orlikbackend.pitch.PitchService
import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException
import com.orlikteam.orlikbackend.reservation.Reservation
import com.orlikteam.orlikbackend.reservation.ReservationRepository
import com.orlikteam.orlikbackend.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest
class PitchServiceSpec extends Specification {

    private static final Double USER_LATITUDE = 20.5
    private static final Double USER_LONGTITUDE = 20.5

    private PitchService pitchService
    private PitchRepository pitchRepository

    def setup() {
        pitchRepository = Mock(PitchRepository)
        pitchService = new PitchService(pitchRepository)
    }

    def "should return list with one pitch when getting all pitches"() {
        given: "table with one pitch in db"
        def pitch = buildPitch(1, "pitch", 65.0, 65.0, new ArrayList())
        pitchRepository.findAll() >> List.of(pitch)

        when: "user get list of pitches"
        def pitches = pitchService.getAllPitches()

        then: "list should contain one pitch"
        pitches.size() == 1
        and:
        pitches.get(0).getPitchId() == 1
        and:
        pitches.get(0).getPitchName() == "pitch"
    }

    def "should return empty list when getting all pitches"() {
        given: "empty list of pitch"
        pitchRepository.findAll() >> List.of()

        when: "user get empty list of pitches"
        def pitches = pitchService.getAllPitches()

        then: "exception should be thrown"
        pitches.size() == 0
    }

    def "should thrown PitchNotFoundException when getting nearest pitch for empty list"() {
        given: "empty list of pitch"
        pitchRepository.findAll() >> List.of()

        when: "user get nearest pitch from empty list"
        pitchService.getNearestPitch(USER_LATITUDE, USER_LONGTITUDE)

        then: "exception should be thrown"
        thrown(PitchNotFoundException)
    }

    def "should return nearest pitch"() {
        given: "list of pitches contains two pitches without reservations"
        def pitch1 = buildPitch(1, "pitch1", USER_LATITUDE + 10.0, USER_LONGTITUDE + 10.0, new ArrayList())
        def pitch2 = buildPitch(2, "pitch2", USER_LATITUDE+2.022, USER_LONGTITUDE+2.0, new ArrayList())
        pitchRepository. findAll() >> List.of(pitch1, pitch2)

        when: "get nearest pitch"
        PitchResponseDto pitch = pitchService.getNearestPitch(USER_LATITUDE, USER_LONGTITUDE)

        then: "return nearest pitch"
        pitch.getPitchId() == 2
    }

    def "should return second nearest pitch, first is unavailable"() {
        given: "list of pitches contains two pitches, the nearest is unavailable"
        def pitch1 = buildPitch(1, "pitch1", USER_LATITUDE + 10.0, USER_LONGTITUDE + 10.0, new ArrayList())

        List<Reservation> pitch2Reservation = new ArrayList<>()
        pitch2Reservation.add(buildReservation(LocalDate.now(), LocalTime.now().plusHours(1), LocalTime.now().plusHours(2)))
        def pitch2 = buildPitch(2, "pitch2", USER_LATITUDE+2.022, USER_LONGTITUDE+2.0, pitch2Reservation)
        pitchRepository. findAll() >> List.of(pitch1, pitch2)

        when: "get nearest pitch"
        PitchResponseDto pitch = pitchService.getNearestPitch(USER_LATITUDE, USER_LONGTITUDE)

        then: "return second nearest pitch"
        pitchRepository.findAll().size() == 2
        pitch.getPitchId() == 1
    }




    private static Pitch buildPitch(Integer id, String name, Double latitude, Double longitude, List<Reservation> reservations) {
        return Pitch
                .builder()
                .pitchId(id)
                .pitchName(name)
                .latitude(latitude)
                .longitude(longitude)
                .reservations(reservations)
                .build()
    }

    private static Reservation buildReservation(LocalDate date, LocalTime startHour, LocalTime endHour) {
        return Reservation
                .builder()
                .reservationDate(date)
                .startHour(startHour)
                .endHour(endHour)
                .build()
    }
}
