package com.orlikteam.orlikbackend.reservation;

import com.orlikteam.orlikbackend.pitch.Pitch;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name="reservations")
public class Reservation {
    @Column(name="reservation_id")
        private Integer reservationId;
    @Column(name="which_user")
        private String whichUser;
    @Column(name="which_pitch")
        private Integer whichPitch;
    @Column(name="reservation_date")
        private Instant reservationDate;
    @Column(name="start_hour")
        private Instant startHour;
    @Column(name="end_hour")
        private Instant endHour;
   /* @OneToOne
    @JoinColumn(name="which_pitch")
        private Pitch pitch;*/
}
