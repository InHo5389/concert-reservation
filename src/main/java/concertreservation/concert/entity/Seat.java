package concertreservation.concert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertScheduleId;
    private String seatNumber;
    private SeatStatus seatStatus;
    private int seatPrice;

    public static Seat create(Long concertScheduleId, String seatNumber, int seatPrice) {
        Seat seat = new Seat();
        seat.concertScheduleId = concertScheduleId;
        seat.seatNumber = seatNumber;
        seat.seatStatus = SeatStatus.AVAILABLE;
        seat.seatPrice = seatPrice;
        return seat;
    }
}
