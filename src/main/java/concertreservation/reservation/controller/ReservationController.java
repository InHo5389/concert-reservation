package concertreservation.reservation.controller;

import concertreservation.reservation.controller.request.ConcertSeatReservationRequest;
import concertreservation.reservation.service.ReservationService;
import concertreservation.reservation.service.response.PaymentResponse;
import concertreservation.reservation.service.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/concerts/reservation")
    public ReservationResponse reservation(@RequestBody ConcertSeatReservationRequest request){
        return reservationService.reservation(request.getUserId(), request.getConcertScheduleId(), request.getSeatId());
    }

    @PostMapping("/concerts/payment")
    public PaymentResponse payment(@RequestBody Long reservationId){
        return reservationService.payment(reservationId);
    }
}
