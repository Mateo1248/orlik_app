package com.orlikteam.orlikbackend.pitch.e2e

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.orlikteam.orlikbackend.pitch.Pitch
import com.orlikteam.orlikbackend.pitch.PitchRepository
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
import spock.lang.Unroll

import javax.servlet.Filter

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class PitchResourceSpec extends Specification {
    @Autowired
    private WebApplicationContext context

    @Autowired
    private Filter springSecurityFilterChain

    @Autowired
    private PitchRepository pitchRepository

    private MockMvc mockMvc

    private ObjectMapper mapper

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        mapper = new ObjectMapper()
    }

    def "should return 200 with list of pitches when getting all pitches"() {
        given:
        pitchRepository.save(getNewPitch("Dembowskiego", 40, 50))
        pitchRepository.save(getNewPitch("Spoldzielcza", 50, 60))

        when:
        def result = performMockMvcGetRequest("/pitches")

        then:
        result.andExpect(status().isOk())

        List<Pitch> pitches = mapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<List<Pitch>>() {})
        pitches.size() == 2
    }

    def "should return 200 with empty list when there are no pitches in database"() {
        given:
        pitchRepository.deleteAll()

        when:
        def result = performMockMvcGetRequest("/pitches")

        then:
        result.andExpect(status().isOk())

        List<Pitch> pitches = mapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<List<Pitch>>() {})
        pitches.size() == 0
    }

    def "should return 200 when adding pitch"() {
        when:
        def result = performMockMvcPostRequest("/pitches", pitchJson(getNewPitch("Spoldzielcza", 40, 50)))

        then:
        result.andExpect(status().isOk())
    }

    @Unroll
    def "should return 400 when adding pitch with null #property"() {
        when:
        def result = performMockMvcPostRequest("/pitches", pitchJson(getNewPitch(name, latitude, longitude)))

        then:
        result.andExpect(status().isBadRequest())

        where:
        property    | name           | latitude | longitude
        "name"      | null           | 40       | 50
        "latitude"  | "Spoldzielcza" | null     | 50
        "longitude" | "Spoldzielcza" | 40       | null
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

    private ResultActions performMockMvcPostRequest(String url, String body) {
        return mockMvc.perform(post(url)
                .contentType("application/json")
                .content(body))
    }

    private ResultActions performMockMvcGetRequest(String url) {
        return mockMvc.perform(get(url))
    }

}
