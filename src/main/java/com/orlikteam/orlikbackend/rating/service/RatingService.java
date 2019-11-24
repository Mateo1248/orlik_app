package com.orlikteam.orlikbackend.rating.service;

import com.orlikteam.orlikbackend.pitch.Pitch;
import com.orlikteam.orlikbackend.pitch.PitchRepository;
import com.orlikteam.orlikbackend.pitch.exception.PitchNotFoundException;
import com.orlikteam.orlikbackend.rating.dto.RatingAverageDto;
import com.orlikteam.orlikbackend.rating.dto.RatingRequestDto;
import com.orlikteam.orlikbackend.rating.dto.RatingResponseDto;
import com.orlikteam.orlikbackend.rating.entity.Rating;
import com.orlikteam.orlikbackend.rating.repository.RatingRepository;
import com.orlikteam.orlikbackend.user.User;
import com.orlikteam.orlikbackend.user.UserRepository;
import com.orlikteam.orlikbackend.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RatingService {

    private final PitchRepository pitchRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public RatingService(PitchRepository pitchRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        this.pitchRepository = pitchRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    public RatingResponseDto addPitchRating(RatingRequestDto ratingDto) {
        var pitch = getPitch(ratingDto.getPitchId());
        var user = getUser(ratingDto.getUserId());
        var rating = ratingRepository.save(buildRating(pitch, user, ratingDto.getValue()));
        return builtRatingResponse(rating);
    }

    public RatingAverageDto getAverageRating(Integer pitchId) {
        var pitch = pitchRepository.findById(pitchId).orElseThrow(PitchNotFoundException::new);
        var ratings = ratingRepository.findAllByPitchId(pitch);
        var average = ratings
                .stream()
                .map(Rating::getValue)
                .mapToInt(Integer::intValue)
                .summaryStatistics()
                .getAverage();
        return buildRatingAverageDto(pitchId, average);
    }

    private RatingAverageDto buildRatingAverageDto(Integer pitchId, double average) {
        return RatingAverageDto.builder().pitchId(pitchId).averageRating(average).build();
    }

    private RatingResponseDto builtRatingResponse(Rating rating) {
        return RatingResponseDto.builder()
                .ratingId(rating.getRatingId())
                .pitchId(rating.getPitchId().getPitchId())
                .userId(rating.getUserId().getUserLogin())
                .value(rating.getValue())
                .build();
    }

    private Rating buildRating(Pitch pitch, User user, int value) {
        return Rating
                .builder()
                .pitchId(pitch)
                .userId(user)
                .value(value)
                .build();
    }

    private User getUser(String userId) {
        var maybeUser = userRepository.findById(userId);
        return maybeUser.orElseThrow(UserNotFoundException::new);
    }

    private Pitch getPitch(Integer pitchId) {
        var maybePitch = pitchRepository.findById(pitchId);
        return maybePitch.orElseThrow(PitchNotFoundException::new);
    }
}
