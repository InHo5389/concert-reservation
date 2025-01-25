package concertreservation.concert.repository;

import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.service.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Override
    public ConcertSchedule save(ConcertSchedule concertSchedule) {
        return concertScheduleJpaRepository.save(concertSchedule);
    }

    @Override
    public boolean existsByConcertIdAndConcertDate(Long concertId, LocalDate concertDate) {
        return concertScheduleJpaRepository.existsByConcertIdAndConcertDate(concertId, concertDate);
    }
}
