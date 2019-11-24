package com.orlikteam.orlikbackend.rating.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RatingAverageDto {

    private Integer pitchId;
    private Double averageRating;
}
