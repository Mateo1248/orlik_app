package com.orlikteam.orlikbackend.pitch;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PitchService {

    private final PitchRepository pitchRepository;

    public PitchService(PitchRepository pitchRepository) {
        this.pitchRepository = pitchRepository;
    }

    @Transactional
    public void addPitch(Pitch pitch) {
        pitchRepository.save(pitch);
    }

    @Transactional
    public List<Pitch> getAllPitches() {
        return pitchRepository.findAll();
    }
}