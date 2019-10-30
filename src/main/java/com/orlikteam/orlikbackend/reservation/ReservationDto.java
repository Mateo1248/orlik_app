package com.orlikteam.orlikbackend.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReservationDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startHour;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endHour;
    private String whichUser;
    private Integer whichPitch;
}
