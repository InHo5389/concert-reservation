package concertreservation.reservation.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSeatReservationRequest {
    private Long userId;
    private Long concertScheduleId;
    private Long seatId;
}
