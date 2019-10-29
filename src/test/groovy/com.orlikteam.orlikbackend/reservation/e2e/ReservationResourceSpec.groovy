package com.orlikteam.orlikbackend.reservation.e2e

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.orlikteam.orlikbackend.pitch.Pitch
import com.orlikteam.orlikbackend.reservation.Reservation
import com.orlikteam.orlikbackend.reservation.ReservationRepository
import com.orlikteam.orlikbackend.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class ReservationResourceSpec extends Specification {

    @Autowired
    private WebApplicationContext context

    @Autowired
    private Filter securityChainFilter

    @Autowired
    private ReservationRepository reservationRepository

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        performMockMvcPostRequest("/users", userJson(getNewUser("test@test.com", "test")))
        performMockMvcPostRequest("/pitches", pitchJson(getNewPitch("Spoldzielcza", 40, 50)))
    }

    def "should return 200 when booking the reservation"() {
    }

    def "should return 404 when booking reservation with non-existent pitch id"() {

    }

    def "should return 404 when booking reservation with non-existent user id"() {

    }

    def "should return 400 when booking reservation with null #property"() {

    }

    def "should return 409 when booking reservation that is in conflict with other"() {

    }

    def "should return 200 with list of reservations"() {

    }

    def "should return 404 when getting list of reservations for non-existent pitch"() {

    }

    private static Reservation getNewReservation(LocalDate date, LocalTime startTime, LocalTime endTime, User user, Pitch pitch) {
        return Reservation
                .builder()
                .reservationDate(date)
                .startHour(startTime)
                .endHour(endTime)
                .whichUser(user)
                .whichPitch(pitch)
                .build()
    }

    private static Integer getPitchId(ResultActions pitchResult) {
        def mapper = new ObjectMapper()
        Pitch pitch = mapper.readValue(pitchResult.andReturn().getResponse().getContentAsString(), new TypeReference<Pitch>() {
        })
        return pitch.pitchId
    }

    private static String reservationJson(Reservation reservation) {
        def mapper = new ObjectMapper()
        mapper.writeValueAsString(reservation)
    }

    private ResultActions performMockMvcPostRequest(String url, String body) {
        return mockMvc.perform(post(url)
                .contentType("application/json")
                .content(body))
    }

    private static Pitch getNewPitch(String name, Double latitude, Double longitude) {
        return Pitch
                .builder()
                .pitchName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()
    }

    private static String pitchJson(Pitch pitch) {
        def mapper = new ObjectMapper()
        return mapper.writeValueAsString(pitch)
    }

    private static final userJson(User user) {
        def mapper = new ObjectMapper()
        return mapper.writeValueAsString(user)
    }

    private static final User getNewUser(String login, String password) {
        return User.builder()
                .userLogin(login)
                .userPassword(password)
                .build()
    }
}
