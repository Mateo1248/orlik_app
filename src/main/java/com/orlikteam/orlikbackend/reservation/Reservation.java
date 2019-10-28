package com.orlikteam.orlikbackend.reservation;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="reservation_id")
    @NotNull
    private Integer reservationId;

    @Column(name="which_user")
    @NotBlank
    private String whichUser;

    @Column(name="which_pitch")
    @NotNull
    private Integer whichPitch;

    @Column(name="reservation_date")
    @NotNull
    private LocalDate reservationDate;

    @Column(name="start_hour")
    @NotNull
    private LocalTime startHour;

    @Column(name="end_hour")
    @NotNull
    private LocalTime endHour;

    //relations ?

}
