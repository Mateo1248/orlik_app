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


    /**
     * method receives request for pitch creation, redirects the request to pitch service
     * @param pitch is an object made from: name, latitude and longitude
     * @return id and name of already added pitch got from service
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PitchResponseDto addPitch(@RequestBody @Valid Pitch pitch) {
        return pitchService.addPitch(pitch);
    }


    /**
     * method receives request for getting pitches, redirects the request to pitch service
     * @return list of all existing pitches got from service
     */
    @GetMapping
    public List<Pitch> getAllPitches() {
        return pitchService.getAllPitches();
    }

    @GetMapping("/nearest/{latitude}/{longtitude}")
    public PitchResponseDto getNearestPitch(@PathVariable("latitude") Double latitude, @PathVariable("longtitude") Double longtitude) {
        return pitchService.getNearestPitch(latitude, longtitude);
    }
}
