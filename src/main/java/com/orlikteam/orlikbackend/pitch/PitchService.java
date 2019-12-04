package com.orlikteam.orlikbackend.pitch;

import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException;
import com.orlikteam.orlikbackend.reservation.Reservation;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

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
     * method is used to get the closest and currently available pitch from app
     * @param userLatitude is latitude taken from user's localization
     * @param userLongtitude is longitude taken from user's localization
     * @return id and name of already found pitch which is available and the nearest to the user's localization (from all pitches) given from repository
     */
    public PitchResponseDto getNearestPitch(Double userLatitude, Double userLongtitude) {
        List<Pitch> pitches = pitchRepository.findAll();

        if (pitches.size() == 0)
            throw new PitchNotFoundException();

        Map<Double, Pitch> distancePitch = new TreeMap<>();

        for(Pitch pitch : pitches) {
            Double distance = countDistanceByCoordinates(userLatitude, userLongtitude, pitch.getLatitude(), pitch.getLongitude());
            distancePitch.put(distance, pitch);
        }

        var pitch = distancePitch
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow(PitchNotFoundException::new)
                .getValue();

        return buildPitchResponseDto(pitch);
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

    private Double countDistanceByCoordinates(Double userLatitude, Double userLongtitude, Double pitchLatitude, Double pitchLongtitude) {
        return Math.sqrt(
                Math.pow((pitchLatitude - userLatitude), 2) +
                Math.pow((Math.cos((userLatitude*Math.PI)/180) *(pitchLongtitude - userLongtitude) ), 2)) *
                (40075.704 / 360);
    }
}