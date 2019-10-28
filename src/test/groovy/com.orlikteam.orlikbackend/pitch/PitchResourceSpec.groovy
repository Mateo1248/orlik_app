package com.orlikteam.orlikbackend.pitch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PitchResourceSpec extends Specification {

    @Autowired
    private PitchResource pitchResource

    def "should return empty list when getting all pitches"() {
        when: "user get empty list of pitches"
        def pitches = pitchResource.getAllPitches()

        then: "list size should be zero"
        pitches.size() == 0
    }

    def "should return list with one pitch when getting all pitches"() {
        given:
        def pitch = getNewPitch(1, "pitch", 65.0, 65.0)
        pitchResource.addPitch(pitch)

        when: "user get list of pitches"
        def pitches = pitchResource.getAllPitches()

        then: "list should contain one pitch"
        pitches.size() == 1
        and:
        pitches.get(0).getPitchId() == 1
        and:
        pitches.get(0).getPitchName() == "pitch"
    }

    private static Pitch getNewPitch(Integer id, String name, Double latitude, Double longtitude) {
        return Pitch
                .builder()
                .pitchId(id)
                .pitchName(name)
                .latitude(latitude)
                .longitude(longtitude)
                .build()
    }
}
