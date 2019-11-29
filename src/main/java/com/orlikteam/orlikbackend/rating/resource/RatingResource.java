package com.orlikteam.orlikbackend.rating.resource;

import com.orlikteam.orlikbackend.rating.dto.RatingAverageDto;
import com.orlikteam.orlikbackend.rating.service.RatingService;
import com.orlikteam.orlikbackend.rating.dto.RatingRequestDto;
import com.orlikteam.orlikbackend.rating.dto.RatingResponseDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingResource {

    private final RatingService ratingService;

    public RatingResource(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    /**
     * method receives request for rating creation, redirects the request to rating service
     * @param ratingRequestDto is an object made from: pitchId, userId and value of rate
     * @return object made from: ratingId, pitchId, userId and value got from service
     */
    @PostMapping
    public RatingResponseDto addRating(@RequestBody @Validated RatingRequestDto ratingRequestDto) {
        return ratingService.addPitchRating(ratingRequestDto);
    }


    /**
     * method receives request for getting rating, redirects the request to rating service
     * @param pitchId is a pitch id from which we are eager to get average rating
     * @return pitch id and its average rating got from service
     */
    @GetMapping
    public RatingAverageDto getAverageRating(@RequestParam("pitchId") Integer pitchId) {
        return ratingService.getAverageRating(pitchId);
    }
}
