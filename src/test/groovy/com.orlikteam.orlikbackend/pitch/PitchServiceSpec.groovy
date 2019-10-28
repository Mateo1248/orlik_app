package com.orlikteam.orlikbackend.pitch

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PitchServiceSpec extends Specification {

    private PitchRepository pitchRepository
    private PitchService pitchService

    def setup() {
        pitchRepository = Mock(PitchRepository)
        pitchService = new PitchService(pitchRepository)
    }

    def "should return list with one pitch when getting all pitches"() {
        given: "table with one pitch in db"
        def pitch = getNewPitch(1, "pitch", 65.0, 65.0)
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
