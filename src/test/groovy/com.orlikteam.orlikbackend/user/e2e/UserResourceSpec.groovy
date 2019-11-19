package com.orlikteam.orlikbackend.user.e2e

import com.fasterxml.jackson.databind.ObjectMapper
import com.orlikteam.orlikbackend.user.User
import com.orlikteam.orlikbackend.user.UserDto
import com.orlikteam.orlikbackend.user.UserRepository
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class UserResourceSpec extends Specification {

    private static final String TEST_LOGIN = "test@example.com"
    private static final String TEST_PASSWORD = "test"

    @Autowired
    private WebApplicationContext context

    @Autowired
    private Filter springSecurityFilterChain

    @Autowired
    private UserRepository userRepository

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "should return 201 when registering user properly"() {
        given:
        performMockMvcDeleteRequest("/users/$TEST_LOGIN")

        when:
        def result = performMockMvcPostRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))

        then:
        result.andExpect(status().isCreated())

        cleanup:
        userRepository.deleteById(TEST_LOGIN)
    }

    def "should return 400 when registering user with login violating email regex"() {
        when:
        def result = performMockMvcPostRequest("/users", userJson("testexample.com", "tester"))

        then:
        result.andExpect(status().isBadRequest())
    }

    @Unroll
    def "should return 400 when registering user with null #property"() {
        when:
        def result = performMockMvcPostRequest("/users", userJson(login, password))

        then:
        result.andExpect(status().isBadRequest())

        where:
        property   | login                | password
        "login"    | null                 | "test"
        "password" | "tester@example.com" | null
    }

    def "should return 409 when registering already existing user"() {
        given:
        performMockMvcPostRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))

        when:
        def result = performMockMvcPostRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))

        then:
        result.andExpect(status().isConflict())

        cleanup:
        userRepository.deleteById(TEST_LOGIN)
    }

    def "should return 404 when deleting non-existent user"() {
        given:
        performMockMvcPostRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))

        when:
        def deleteResult = performMockMvcDeleteRequest("/users/user@todelete.com")

        then:
        deleteResult.andExpect(status().isNotFound())

        cleanup:
        userRepository.deleteById(TEST_LOGIN)
    }

    def "should return 200 when deleting user"() {
        given:
        performMockMvcPostRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))

        when:
        def deleteResult = performMockMvcDeleteRequest("/users/$TEST_LOGIN")

        then:
        deleteResult.andExpect(status().isOk())
    }

    def "should return 200 with user login when getting user by login"() {
        given:
        performMockMvcPostRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))

        when:
        def result = performMockMvcGetRequest("/users/$TEST_LOGIN")

        then:
        def response = result.andReturn()
        response.getResponse().getContentAsString() == TEST_LOGIN

        cleanup:
        performMockMvcDeleteRequest("/users/$TEST_LOGIN")
    }

    def "should return 404 when getting user by login"() {
        when:
        def result = performMockMvcGetRequest("/users/$TEST_LOGIN")

        then:
        result.andExpect(status().isNotFound())
    }

    def "should return 404 when updating non existing user"() {
        when:
        def result = performMockMvcUpdateRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))

        then:
        result.andExpect(status().isNotFound())
    }

    def "should return 200 when properly updating an user"() {
        given:
        performMockMvcPostRequest("/users", userJson(TEST_LOGIN, TEST_PASSWORD))
        def newPassword = "newPassword"

        when:
        def result = performMockMvcUpdateRequest("/users", userJson(TEST_LOGIN, newPassword))

        then:
        result.andExpect(status().isOk())

        cleanup:
        performMockMvcDeleteRequest("/users/$TEST_LOGIN")
    }



    private static String userJson(String login, String password) {
        def user = UserDto.builder().userLogin(login).userPassword(password).build()
        def mapper = new ObjectMapper()
        mapper.writeValueAsString(user)
    }

    private ResultActions performMockMvcPostRequest(String url, String body) {
        return mockMvc.perform(post(url)
                .contentType("application/json")
                .content(body))
    }

    private ResultActions performMockMvcDeleteRequest(String url) {
        return mockMvc.perform(delete(url))
    }

    private ResultActions performMockMvcGetRequest(String url) {
        return mockMvc.perform(get(url))
    }

    private ResultActions performMockMvcUpdateRequest(String url, String body) {
        return mockMvc.perform(patch(url)
                .contentType("application/json")
                .content(body))
    }

    /*private static UserDto buildUserDto(String login, String password) {
        return UserDto
                .builder()
                .userLogin(login)
                .userPassword(password)
                .build()
    }

    private static String buildUserDtoJson(UserDto userDto) {
        return """
        {
            "userLogin": "${userDto.userLogin}",
            "userPassword": "${userDto.userPassword}",
        }
        """.stripIndent()
    }*/
}
