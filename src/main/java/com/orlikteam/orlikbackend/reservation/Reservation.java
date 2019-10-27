package com.orlikteam.orlikbackend.reservation;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="reservations")
public class Reservation {

    @Id
    @Column(name="reservation_id")
    @NotBlank
    private Integer reservationId;

    @Column(name="which_user")
    @NotBlank
    private String whichUser;

    @Column(name="which_pitch")
    @NotBlank
    private Integer whichPitch;

    @Column(name="reservation_date")
    @NotBlank
    private Instant reservationDate;

    @Column(name="start_hour")
    @NotBlank
    private Instant startHour;

    @Column(name="end_hour")
    @NotBlank
    private Instant endHour;

    //relations

}
