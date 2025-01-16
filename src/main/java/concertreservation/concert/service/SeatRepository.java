package concertreservation.concert.service;

import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;

import java.util.List;

public interface SeatRepository {
    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus);
}
