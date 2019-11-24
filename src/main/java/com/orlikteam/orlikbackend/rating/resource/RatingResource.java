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

    @PostMapping
    public RatingResponseDto addRating(@RequestBody @Validated RatingRequestDto ratingRequestDto) {
        return ratingService.addPitchRating(ratingRequestDto);
    }

    @GetMapping
    public RatingAverageDto getAverageRating(@RequestParam("pitchId") Integer pitchId) {
        return ratingService.getAverageRating(pitchId);
    }
}
