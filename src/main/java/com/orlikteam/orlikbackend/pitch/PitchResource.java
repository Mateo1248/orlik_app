package com.orlikteam.orlikbackend.pitch;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pitches")
public class PitchResource {

    private final PitchService pitchService;

    public PitchResource(PitchService pitchService) {
        this.pitchService = pitchService;
    }

    @PostMapping
    public void addPitch(@RequestBody @Valid Pitch pitch) {
        pitchService.addPitch(pitch);
    }

    @GetMapping("/all")
    public List<Pitch> getAllPitches() {
        return pitchService.getAllPitches();
    }
}
