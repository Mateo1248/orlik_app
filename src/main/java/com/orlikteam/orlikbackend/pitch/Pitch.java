package com.orlikteam.orlikbackend.pitch;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="pitches")
public class Pitch {

    @Column(name="pitch_id")
    @Id
    @GeneratedValue
    private Integer pitchId;

    @Column(name="pitch_name")
    @NotBlank
    private String pitchName;

    @Column(name="latitude")
    @NotNull
    private Double latitude;

    @Column(name="longitude")
    @NotNull
    private Double longitude;
}
