package concertreservation.concert.repository;

import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.service.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Override
    public List<ConcertSchedule> findByConcertId(Long concertId) {
        return concertScheduleJpaRepository.findByConcertId(concertId);
    }
}
