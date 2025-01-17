package concertreservation.concert.repository;

import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus) {
        return seatJpaRepository.findByConcertScheduleIdAndSeatStatus(concertScheduleId, seatStatus);
    }

    @Override
    public Optional<Seat> findByIdAndConcertScheduleId(Long concertScheduleId, Long seatId) {
        return seatJpaRepository.findByIdAndConcertScheduleId(concertScheduleId, seatId);
    }
}
