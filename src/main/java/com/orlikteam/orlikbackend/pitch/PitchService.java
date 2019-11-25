package com.orlikteam.orlikbackend.pitch;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PitchService {

    private final PitchRepository pitchRepository;

    public PitchService(PitchRepository pitchRepository) {
        this.pitchRepository = pitchRepository;
    }


    /**
     * method is used to add a new pitch to app
     * @param pitch is an object made from: name, latitude and longitude
     * @return id and name as a response of properly added pitch given from repository
     */
    public PitchResponseDto addPitch(Pitch pitch) {
        var createdPitch = pitchRepository.save(pitch);
        return buildPitchResponseDto(createdPitch);
    }


    /**
     * method is used to get existing pitches from app
     * @return list of all existing pitches as a response given from repository
     */
    public List<Pitch> getAllPitches() {
        return pitchRepository.findAll();
    }

    private static PitchResponseDto buildPitchResponseDto(Pitch pitch) {
        return PitchResponseDto
                .builder()
                .pitchId(pitch.getPitchId())
                .pitchName(pitch.getPitchName())
                .build();
    }
}