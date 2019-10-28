package com.orlikteam.orlikbackend.reservation.e2e

import com.orlikteam.orlikbackend.reservation.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import javax.servlet.Filter

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
}
