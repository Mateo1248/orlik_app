package com.orlikteam.orlikbackend.pitch

import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PitchServiceSpec extends Specification {

    @Mock
    private PitchRepository pitchRepository

    private PitchService pitchService

    def setup() {
        pitchService = new PitchService(pitchRepository)
    }

    def "check getAllPitches response for non empty list"() {
        given: "table with one pitch in db"
        def pitch = Pitch.builder()
                .pitchId(1)
                .pitchName("pitch")
                .longitude(65.0)
                .latitude(65.0)
                .build()
        Mockito.when(pitchRepository.findAll()).thenReturn(List.of(pitch))

        when: "user get list of pitches"
        def pitches = pitchService.getAllPitches()

        then: "list should contain one pitch"
        pitches.size() == 1
        and:
        pitches.get(0).getPitchId() == 1
        and:
        pitches.get(0).getPitchName() == "pitch"
    }

    def "check if getAllPitches throw exception for empty list"() {
        given: "empty table from db"
        when: "user get empty list of pitches"
            pitchService.getAllPitches()
        then: "exception should be thrown"
            thrown(PitchNotFoundException)
    }
}
