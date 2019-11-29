package com.orlikteam.orlikbackend.reservation;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(ReservationService reservationService) {
        this.reservationService=reservationService;
    }


    /**
     * method receives request for reservation creation, redirects the request to reservation service
     * @param reservation is an object made from: date, start and end hours and chosen pitch given by user
     * @return id of already added reservation got from service
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationIdDto addReservation(@RequestBody @Validated ReservationDto reservation) {
        return reservationService.addReservation(reservation);
    }


    /**
     * method receives request for getting the reservation by chosen pitch and date, redirects the request to reservation service
     * @param whichPitch is a pitch id from which we are eager to get reservations
     * @param reservationDate  is a chosen date from which we are eager to get reservations
     * @return list of reservations existing on selected pitch and date got from service
     */
    @GetMapping
    public List<ReservationDto> getReservationsByPitchIdAndReservationDate(@RequestParam("whichPitch") Integer whichPitch,
                                                                           @RequestParam("reservationDate") @DateTimeFormat(iso = DATE) LocalDate reservationDate) {
        return reservationService.getReservationByPitchIdAndDate(whichPitch, reservationDate);
    }


    /**
     * method receives request for getting the reservation by user login, redirects the request to reservation service
     * @param userLogin is a user login for whom we are eager to get reservations
     * @return list of reservations which current user have got from service
     */
    @GetMapping("/users/{userLogin}")
    public List<ReservationDto> getReservationByUserLogin(@PathVariable String userLogin) {
        return reservationService.getReservationByWhichUser(userLogin);
    }


    /**
     * method receives request for reservation deletion, redirects the request to reservation service
     * @param reservationId is a id of reservation which we are eager to delete from app
     */
    @DeleteMapping(value = "/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable(value = "reservationId") int reservationId) {
        reservationService.cancelReservation(reservationId);
    }
}
