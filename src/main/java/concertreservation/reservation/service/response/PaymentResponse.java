package concertreservation.reservation.service.response;

import concertreservation.reservation.entity.PaymentHistory;
import concertreservation.reservation.entity.Reservation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentResponse {

    private Long paymentId;
    private String concertTitle;
    private String seatNumber;
    private int paymentPoint;
    private LocalDateTime createdAt;

    public static PaymentResponse from(PaymentHistory paymentHistory, Reservation reservation) {
        PaymentResponse response = new PaymentResponse();
        response.paymentId = paymentHistory.getId();
        response.concertTitle = reservation.getConcertTitle();
        response.seatNumber = reservation.getSeatNumber();
        response.paymentPoint = reservation.getReservationPoint();
        response.createdAt = LocalDateTime.now();
        return response;
    }
}
