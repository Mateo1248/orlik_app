package com.orlikteam.orlikbackend.security.e2e

import com.fasterxml.jackson.databind.ObjectMapper
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
import spock.lang.Unroll

import javax.servlet.Filter

import static org.hamcrest.core.StringStartsWith.startsWith
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class AuthServiceSpec extends Specification {

    @Autowired
    private WebApplicationContext webApplicationContext

    @Autowired
    private Filter springSecurityFilterChain

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build()
    }

    def "should return 200 with Authorization header when logging in with correct credentials"() {
        given:
        def userJson = userJson("test@example.com", "test")
        performMockMvcPostRequest("/users", userJson)

        when:
        def result = performMockMvcPostRequest("/login", userJson)

        then:
        with(result) {
            andExpect(status().isOk())
            andExpect(header().string("Authorization", startsWith("Bearer ")))
        }
    }

    def "should return 401 when logging in with incorrect credentials"() {
        when:
        def result = performMockMvcPostRequest("/login", userJson("tester@example.com", "test"))

        then:
        result.andExpect(status().isUnauthorized())
    }

    def "should return 401 when logging in with login violating email regex"() {
        when:
        def result = performMockMvcPostRequest("/login", userJson("testexample.com", "test"))

        then:
        result.andExpect(status().isUnauthorized())
    }

    @Unroll
    def "should return 401 when logging in with null #property"() {
        when:
        def result = performMockMvcPostRequest("/login", userJson(login, password))

        then:
        result.andExpect(status().isUnauthorized())

        where:
        property   | login              | password
        "login"    | null               | "test"
        "password" | "test@example.com" | null
    }

    private static String userJson(String login, String password) {
        def user = User.builder().userLogin(login).userPassword(password).build()
        def mapper = new ObjectMapper()
        mapper.writeValueAsString(user)
    }

    private ResultActions performMockMvcPostRequest(String url, String body) {
        mockMvc
                .perform(post(url)
                        .contentType("application/json")
                        .content(body))
    }
}
