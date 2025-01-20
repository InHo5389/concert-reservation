package concertreservation.concert.entity;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
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

    @Enumerated(value = EnumType.STRING)
    private SeatStatus seatStatus;
    private int seatPrice;

    @Version
    private Long version;

    public Seat(Long concertScheduleId, String seatNumber, SeatStatus seatStatus, int seatPrice) {
        this.concertScheduleId = concertScheduleId;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
        this.seatPrice = seatPrice;
    }

    public static Seat create(Long concertScheduleId, String seatNumber, int seatPrice) {
        Seat seat = new Seat();
        seat.concertScheduleId = concertScheduleId;
        seat.seatNumber = seatNumber;
        seat.seatStatus = SeatStatus.AVAILABLE;
        seat.seatPrice = seatPrice;
        return seat;
    }

    public void validateSeatAvailability(Long scheduleId) {
        if (!this.concertScheduleId.equals(scheduleId)) {
            throw new CustomGlobalException(ErrorType.NOT_FOUNT_CONCERT_SCHEDULE_SEAT);
        }
        if (!isAvailableSeat()) {
            throw new CustomGlobalException(ErrorType.ALREADY_RESERVED_SEAT);
        }
    }

    public boolean isAvailableSeat(){
        return this.seatStatus == SeatStatus.AVAILABLE;
    }

    public void updateSeatStatus(SeatStatus seatStatus){
        this.seatStatus = seatStatus;
    }
}
