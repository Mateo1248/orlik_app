package com.orlikteam.orlikbackend.pitch;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PitchResponseDto {
    private int pitchId;
    private String pitchName;
}
