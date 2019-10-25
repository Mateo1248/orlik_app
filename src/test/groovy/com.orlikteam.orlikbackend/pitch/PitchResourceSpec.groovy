package com.orlikteam.orlikbackend.pitch

import com.orlikteam.orlikbackend.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PitchResourceSpec extends Specification {

    @Autowired
    PitchResource pitchResource

    def "check if getAllPitches throw exception for empty list"() {
        when: "user get empty list of pitches"
            pitchResource.getAllPitches()
        then: "exception should be thrown"
            thrown(NotFoundException)
    }

    def "check getAllPitches response for non empty list"() {
        given:
            def pitch = Pitch.builder()
                .pitchId(1)
                .pitchName("pitch")
                .longitude(65.0)
                .latitude(65.0)
                .build()

        when: "user get list of pitches"
            pitchResource.addPitch(pitch)
            def pitches = pitchResource.getAllPitches()

        then: "list should contain one pitch"
            pitches.size() == 1
        and:
            pitches.get(0).getPitchId() == 1
        and:
            pitches.get(0).getPitchName() == "pitch"
    }

}
