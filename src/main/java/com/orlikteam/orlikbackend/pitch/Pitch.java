package com.orlikteam.orlikbackend.pitch;

import com.orlikteam.orlikbackend.rating.entity.Rating;
import com.orlikteam.orlikbackend.reservation.Reservation;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="pitches")
public class Pitch {

    @Id
    @Column(name="pitch_id")
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

    @Transient
    @OneToMany(mappedBy = "which_pitch")
    private List<Reservation> reservations;

    @Transient
    @OneToMany(mappedBy = "pitch_id")
    private List<Rating> ratings;

}
