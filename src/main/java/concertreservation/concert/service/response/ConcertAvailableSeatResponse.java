package concertreservation.concert.service.response;

import concertreservation.concert.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertAvailableSeatResponse {

    private List<String> seatNumber;

    public static ConcertAvailableSeatResponse from(List<Seat> seats) {
        List<String> seatNumbers = seats.stream()
                .map(Seat::getSeatNumber)
                .toList();
        return new ConcertAvailableSeatResponse(seatNumbers);
    }
}
