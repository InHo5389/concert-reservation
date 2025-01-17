package concertreservation.concert.repository;

import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus);
    Optional<Seat> findByIdAndConcertScheduleId(Long seatId, Long concertScheduleId);
}
