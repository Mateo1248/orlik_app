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


    /**
     * method receives request for getting the nearest, available pitch, redirects the request to pitch service
     * @param latitude is latitude taken from user's localization
     * @param longitude is longitude taken from user's localization
     * @return id and name of already found pitch which is available and the nearest to the user's localization (from all pitches)
     */
    @GetMapping("/nearest/{latitude}/{longitude}")
    public PitchResponseDto getNearestPitch(@PathVariable("latitude") Double latitude, @PathVariable("longitude") Double longitude) {
        return pitchService.getNearestPitch(latitude, longitude);
    }
}
