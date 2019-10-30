package com.orlikteam.orlikbackend.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orlikteam.orlikbackend.pitch.Pitch;
import com.orlikteam.orlikbackend.user.User;
import lombok.*;

import javax.persistence.*;
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
    @Column(name="reservation_id")
    @GeneratedValue
    private Integer reservationId;

    @Column(name="reservation_date")
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate reservationDate;

    @Column(name="start_hour")
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startHour;

    @Column(name="end_hour")
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endHour;

    @ManyToOne
    @JoinColumn(name = "which_user", referencedColumnName = "user_login")
    private User whichUser;

    @ManyToOne
    @JoinColumn(name = "which_pitch", referencedColumnName = "pitch_id")
    private Pitch whichPitch;

}
