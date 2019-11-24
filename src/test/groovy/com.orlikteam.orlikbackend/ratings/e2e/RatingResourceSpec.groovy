package com.orlikteam.orlikbackend.ratings.e2e

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.orlikteam.orlikbackend.pitch.Pitch
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
@DirtiesContext
class RatingResourceSpec extends Specification {

    private static final int NON_EXISTENT_PITCH_ID = -1
    private static final String USER_LOGIN = "test@test.com"
    private static final String NON_EXISTENT_USER_LOGIN = "tester@test.com"

    @Autowired
    private WebApplicationContext context

    @Autowired
    private Filter springSecurityFilterChain

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        performMockMvcPostRequest("/users", buildUserJson(getNewUser(USER_LOGIN, "test")))
        performMockMvcPostRequest("/pitches", buildPitchJson(getNewPitch("Spoldzielcza", 40, 50)))
        performMockMvcPostRequest("/pitches", buildPitchJson(getNewPitch("Robotnicza", 44, 50)))
    }

    def "should return 200 when rating pitch"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()

        when:
        def result = performMockMvcPostRequest("/ratings", buildRatingRequestDtoJson(USER_LOGIN, pitchId, 5))

        then:
        with (result) {
            andExpect(status().isOk())
        }
    }

    def "should return 400 when rating pitch with rating out of scale"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()

        when:
        def result = performMockMvcPostRequest("/ratings", buildRatingRequestDtoJson(USER_LOGIN, pitchId, 6))

        then:
        with (result) {
            andExpect(status().isBadRequest())
        }
    }

    def "should return 404 when rating non existent pitch"() {
        when:
        def result = performMockMvcPostRequest("/ratings", buildRatingRequestDtoJson(USER_LOGIN, NON_EXISTENT_PITCH_ID, 5))

        then:
        with (result) {
            andExpect(status().isNotFound())
        }
    }

    def "should return 404 when rating pitch by non existent user"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).first().getPitchId()

        when:
        def result = performMockMvcPostRequest("/ratings", buildRatingRequestDtoJson(NON_EXISTENT_USER_LOGIN, pitchId, 5))

        then:
        with (result) {
            andExpect(status().isNotFound())
        }
    }

    def "should return 404 when getting average rating of non existent pitch"() {
        when:
        def result = performMockMvcGetRequest("/ratings?pitchId=${NON_EXISTENT_PITCH_ID}")

        then:
        result.andExpect(status().isNotFound())
    }

    def "should return 200 with rating average"() {
        given:
        def pitchId = getListOfPitches(performMockMvcGetRequest("/pitches")).get(1).getPitchId()
        performMockMvcPostRequest("/ratings", buildRatingRequestDtoJson(USER_LOGIN, pitchId, 5))
        performMockMvcPostRequest("/ratings", buildRatingRequestDtoJson(USER_LOGIN, pitchId, 4))
        performMockMvcPostRequest("/ratings", buildRatingRequestDtoJson(USER_LOGIN, pitchId, 3))

        when:
        def result = performMockMvcGetRequest("/ratings?pitchId=${pitchId}")

        then:
        with (result) {
            andExpect(status().isOk())
            andExpect(jsonPath('$.averageRating').value(4.0))
        }

    }

    private static String buildRatingRequestDtoJson(String user, int pitchId, int value) {
        return """
        {
            "userId": "${user}",
            "pitchId": "${pitchId}",
            "value": "${value}"
        }
        """.stripIndent()
    }

    private static List<Pitch> getListOfPitches(ResultActions pitchResult) {
        def mapper = new ObjectMapper()
        List<Pitch> pitches = mapper.readValue(pitchResult.andReturn().getResponse().getContentAsString(), new TypeReference<List<Pitch>>() {})
        return pitches
    }

    private ResultActions performMockMvcGetRequest(String url) {
        return mockMvc.perform(get(url)
                .accept("application/json"))
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

    private ResultActions performMockMvcPostRequest(String url, String body) {
        return mockMvc.perform(post(url)
                .contentType("application/json")
                .content(body))
    }
}
