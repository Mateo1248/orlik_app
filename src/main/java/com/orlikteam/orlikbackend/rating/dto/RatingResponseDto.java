package com.orlikteam.orlikbackend.rating.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RatingResponseDto {

    private Integer ratingId;
    private Integer pitchId;
    private String userId;
    private Integer value;
}
