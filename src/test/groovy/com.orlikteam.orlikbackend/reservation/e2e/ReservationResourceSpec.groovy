package com.orlikteam.orlikbackend.reservation.e2e

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.orlikteam.orlikbackend.pitch.Pitch
import com.orlikteam.orlikbackend.pitch.PitchResponseDto
import com.orlikteam.orlikbackend.reservation.ReservationRepository
import com.orlikteam.orlikbackend.reservation.ReservationDto
import com.orlikteam.orlikbackend.reservation.ReservationIdDto
import com.orlikteam.orlikbackend.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import javax.servlet.Filter
import java.time.LocalDate
import java.time.LocalTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class ReservationResourceSpec extends Specification {

    private static final int NON_EXISTENT_PITCH_ID = -1
    private static final String USER_LOGIN = "test@test.com"
    private static final String NON_EXISTENT_USER_LOGIN = "tester@test.com"

    @Autowired
    private WebApplicationContext context

    @Autowired
    private Filter springSecurityFilterChain

    @Autowired
    private ReservationRepository reservationRepository

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        performMockMvcPostRequest("/users", buildUserJson(getNewUser(USER_LOGIN, "test")))
        performMockMvcPostRequest("/pitches", buildPitchJson(getNewPitch("Spoldzielcza", 40, 50)))
    }

    def "should return 201 when booking the reservation"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()

        when:
        def result = performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(LocalDate.of(2019, 11, 15), LocalTime.of(11, 00), LocalTime.of(12, 00), USER_LOGIN, pitchId)))

        then:
        with(result) {
            andExpect(status().isCreated())
        }
        getReservationIdOf(result)
    }

    def "should return 404 when booking reservation with non-existent pitch id"() {
        when:
        def result = performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(LocalDate.of(2019, 11, 15), LocalTime.of(11, 00), LocalTime.of(12, 00), USER_LOGIN, NON_EXISTENT_PITCH_ID)))

        then:
        with(result) {
            andExpect(status().isNotFound())
        }
    }

    def "should return 404 when booking reservation with non-existent user id"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()

        when:
        def result = performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(LocalDate.of(2019, 11, 15), LocalTime.of(11, 00), LocalTime.of(12, 00), NON_EXISTENT_USER_LOGIN, pitchId)))

        then:
        with(result) {
            andExpect(status().isNotFound())
        }
    }

    def "should return 400 when booking reservation with null #property"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()

        when:
        def result = performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(reservationDate, startHour, endHour, userLogin, pitchId)))

        then:
        with(result) {
            andExpect(status().isBadRequest())
        }

        where:
        property           | startHour            | endHour              | reservationDate            | userLogin
        "startHour "       | null                 | LocalTime.of(12, 00) | LocalDate.of(2019, 11, 15) | USER_LOGIN
        "endHour "         | LocalTime.of(11, 00) | null                 | LocalDate.of(2019, 11, 15) | USER_LOGIN
        "reservationDate " | LocalTime.of(11, 00) | LocalTime.of(12, 00) | null                       | USER_LOGIN
    }

    def "should return 409 when booking reservation that is in conflict with other"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()
        performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(LocalDate.of(2019, 11, 15), LocalTime.of(11, 00), LocalTime.of(12, 00), USER_LOGIN, pitchId)))

        when:
        def result = performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(LocalDate.of(2019, 11, 15), LocalTime.of(11, 00), LocalTime.of(12, 00), USER_LOGIN, pitchId)))

        then:
        with(result) {
            andExpect(status().isConflict())
        }
    }

    def "should return 200 with list of reservations"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()
        def date = LocalDate.of(2019, 11, 15)
        performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(date, LocalTime.of(11, 00), LocalTime.of(12, 00), USER_LOGIN, pitchId)))

        when:
        def result = performMockMvcGetRequest("/reservations?whichPitch=${pitchId}&reservationDate=${date}")

        then:
        with(result) {
            andExpect(status().isOk())
        }
    }

    def "should return 404 when getting list of reservations for non-existent pitch"() {
        given:
        def date = LocalDate.of(2019, 11, 15)

        when:
        def result = performMockMvcGetRequest("/reservations?whichPitch=${NON_EXISTENT_PITCH_ID}&reservationDate=${date}")

        then:
        with(result) {
            andExpect(status().isNotFound())
        }
    }

    def "should return 204 when cancelling reservation"() {
        given:
        reservationRepository.deleteAll()
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()
        performMockMvcPostRequest("/reservations", buildReservationDtoJson(getNewReservationDto(LocalDate.of(2019, 11, 15), LocalTime.of(11, 00), LocalTime.of(12, 00), USER_LOGIN, pitchId)))
        def reservationId = reservationRepository.findAll().first().getReservationId()

        when:
        def result = performMockMvcDeleteRequest("/reservations/${reservationId}")

        then:
        with(result) {
            andExpect(status().isNoContent())
        }
    }

    def "should return 404 when cancelling non existent reservation"() {
        given:
        reservationRepository.deleteAll()
        def reservationId = -1

        when:
        def result = performMockMvcDeleteRequest("/reservations/${reservationId}")

        then:
        with(result) {
            andExpect(status().isNotFound())
        }
    }

    private ResultActions performMockMvcGetRequest(String url) {
        return mockMvc.perform(get(url)
                .accept("application/json"))
    }

    private ResultActions performMockMvcPostRequest(String url, String body) {
        return mockMvc.perform(post(url)
                .contentType("application/json")
                .content(body))
    }

    private ResultActions performMockMvcDeleteRequest(String url) {
        return mockMvc.perform(delete(url)
                .contentType("application/json"))
    }

    private static ReservationDto getNewReservationDto(LocalDate date, LocalTime startTime, LocalTime endTime, String user, int pitch) {
        return ReservationDto
                .builder()
                .reservationDate(date)
                .startHour(startTime)
                .endHour(endTime)
                .whichUser(user)
                .whichPitch(pitch)
                .build()
    }

    private static int getPitchId(ResultActions pitchResult) {
        def mapper = new ObjectMapper()
        PitchResponseDto pitch = mapper.readValue(pitchResult.andReturn().getResponse().getContentAsString(), new TypeReference<PitchResponseDto>() {
        })
        return pitch.pitchId
    }

    private static List<Pitch> getListOfPitches(ResultActions pitchResult) {
        def mapper = new ObjectMapper()
        List<Pitch> pitches = mapper.readValue(pitchResult.andReturn().getResponse().getContentAsString(), new TypeReference<List<Pitch>>() {})
        return pitches
    }

    private static int getReservationIdOf(ResultActions result) {
        def mapper = new ObjectMapper()
        ReservationIdDto reservation = mapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<ReservationIdDto>() {
        })
        return reservation.reservationId
    }

    private static String buildReservationDtoJson(ReservationDto reservationDto) {
        return """
        {
            "reservationDate": "${reservationDto.reservationDate}",
            "startHour": "${reservationDto.startHour}",
            "endHour": "${reservationDto.endHour}",
            "whichPitch": ${reservationDto.whichPitch},
            "whichUser": "${reservationDto.whichUser}"
        }
        """.stripIndent()
    }

    private static Pitch getNewPitch(String name, Double latitude, Double longitude) {
        return Pitch
                .builder()
                .pitchName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()
    }

    private static String buildPitchJson(Pitch pitch) {
        def mapper = new ObjectMapper()
        return mapper.writeValueAsString(pitch)
    }

    private static buildUserJson(User user) {
        def mapper = new ObjectMapper()
        return mapper.writeValueAsString(user)
    }

    private static User getNewUser(String login, String password) {
        return User.builder()
                .userLogin(login)
                .userPassword(password)
                .build()
    }
}
