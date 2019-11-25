package com.orlikteam.orlikbackend.pitch;

import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException;
import org.springframework.stereotype.Service;
import com.orlikteam.orlikbackend.reservation.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PitchService {

    private final PitchRepository pitchRepository;
    private final ReservationRepository reservationRepository;

    public PitchService(PitchRepository pitchRepository, ReservationRepository reservationRepository) {
        this.pitchRepository = pitchRepository;
        this.reservationRepository = reservationRepository;
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
    public PitchResponseDto getNearestPitch(Double userLatitude, Double userLongtitude) {
        List<Pitch> pitches = pitchRepository.findAll();

        if (pitches.size() == 0)
            throw new PitchNotFoundException();

        Map<Double, Pitch> distancePitch = new TreeMap<>();

        for(Pitch pitch : pitches) {
            Double distancet = countDistanceByCoordinates(userLatitude, userLongtitude, pitch.getLatitude(), pitch.getLongitude());

            distancePitch.put(distancet, pitch);
        }

        PitchResponseDto nearest = null;

        for (Iterator key = distancePitch.keySet().iterator(); key.hasNext();) {
            if(pitchIsAvailableNow(distancePitch.get(key))) {
                nearest = buildPitchResponseDto(distancePitch.get(key));
                break;
            }
        }

        if(nearest == null)
            throw new PitchNotFoundException();

        return nearest;
    }

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

    private boolean pitchIsAvailableNow(Pitch pitch) {
        return !reservationRepository.findAllByWhichPitchAndReservationDateIsAndStartHourBeforeAndEndHourAfter(
                pitch.getPitchId(),
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now().plusHours(2))
                .isEmpty();
    }
}