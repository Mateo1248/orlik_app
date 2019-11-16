package com.orlikteam.orlikbackend.reservation;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReservationIdDto {
    private int reservationId;
}
