package com.orlikteam.orlikbackend.pitch;

import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public PitchResponseDto addPitch(@RequestBody @Valid Pitch pitch) {
        return pitchService.addPitch(pitch);
    }

    @GetMapping
    public List<Pitch> getAllPitches() {
        return pitchService.getAllPitches();
    }

    @GetMapping("/nearest/{latitude}/{longtitude}")
    public PitchResponseDto getNearestPitch(@PathVariable("latitude") Double latitude, @PathVariable("longtitude") Double longtitude) {
        return pitchService.getNearestPitch(latitude, longtitude);
    }
}
