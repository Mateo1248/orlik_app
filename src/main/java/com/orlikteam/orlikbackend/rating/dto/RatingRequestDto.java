package com.orlikteam.orlikbackend.rating.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RatingRequestDto {

    private Integer pitchId;
    private String userId;
    @Min(1)
    @Max(5)
    private int value;
}
