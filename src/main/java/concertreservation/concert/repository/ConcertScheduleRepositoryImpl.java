package concertreservation.concert.repository;

import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.service.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Override
    public List<ConcertSchedule> findByConcertId(Long concertId) {
        return concertScheduleJpaRepository.findByConcertId(concertId);
    }

    @Override
    public Optional<ConcertSchedule> findById(Long concertScheduleId) {
        return concertScheduleJpaRepository.findById(concertScheduleId);
    }
}
