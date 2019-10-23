package com.orlikteam.orlikbackend.pitch;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="pitches")
public class Pitch {
    @Column(name="pitch_id")
    @Id
    private Integer pitchId;

    @Column(name="pitch_name")
    private String pitchName;

    @Column(name="latitude")
    private Float latitude;

    @Column(name="longitude")
    private Float longitude;
}
