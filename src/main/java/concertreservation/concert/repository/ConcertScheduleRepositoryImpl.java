package concertreservation.concert.repository;

import concertreservation.concert.service.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
}
