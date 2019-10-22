package com.orlikteam.orlikbackend.pitch;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="pitches")
public class Pitch {
    @Column(name="pitch_id")
        private Integer pitchId;
    @Column(name="pitch_name")
        private String pitchName;
    @Column(name="latitude")
        private Integer latitude;
    @Column(name="longitude")
        private Integer longitude;
}
