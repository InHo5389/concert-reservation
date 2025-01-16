package concertreservation.concert.repository;

import concertreservation.concert.service.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;
}
