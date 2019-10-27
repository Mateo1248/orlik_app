package com.orlikteam.orlikbackend.pitch;

import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException;
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
        List<Pitch> pitches = pitchRepository.findAll();

        if(pitches.size() == 0)
            throw new PitchNotFoundException("Pitches not found.");

        return pitches;
    }
}