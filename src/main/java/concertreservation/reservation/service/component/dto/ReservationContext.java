package concertreservation.reservation.service.component.dto;

import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.user.service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationContext {
    private User user;
    private Concert concert;
    private ConcertSchedule concertSchedule;
    private Seat seat;
}
