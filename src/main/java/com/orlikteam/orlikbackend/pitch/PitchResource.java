package com.orlikteam.orlikbackend.pitch;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pitches")
public class PitchResource {

    private final PitchService pitchService;

    public PitchResource(PitchService pitchService) {
        this.pitchService = pitchService;
    }

    @PostMapping
    public void addPitch(Pitch pitch) {
        pitchService.addPitch(pitch);
    }

    @GetMapping("/all")
    public List<Pitch> getAllPitches() {
        return pitchService.getAllPitches();
    }
}
