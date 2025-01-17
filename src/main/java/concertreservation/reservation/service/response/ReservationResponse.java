package concertreservation.reservation.service.response;

import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.reservation.entity.Reservation;
import concertreservation.user.service.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReservationResponse {

    private Long userId;
    private String username;
    private Long concertId;
    private String title;
    private String singerName;
    private LocalDate concertDate;
    private String seatNumber;
    private int seatPrice;
    private LocalDateTime reservationExpireTime;
    private LocalDateTime createdAt;

    public static ReservationResponse from(Reservation reservation, Concert concert, ConcertSchedule concertSchedule, User user, Seat seat) {
        ReservationResponse response = new ReservationResponse();
        response.userId = user.getId();
        response.username = user.getName();
        response.concertId = concert.getId();
        response.title = concert.getTitle();
        response.singerName = concert.getName();
        response.concertDate = concertSchedule.getConcertDate();
        response.seatNumber = seat.getSeatNumber();
        response.seatPrice = seat.getSeatPrice();
        response.reservationExpireTime = reservation.getExpiredAt();
        response.createdAt = reservation.getCreatedAt();
        return response;
    }
}
