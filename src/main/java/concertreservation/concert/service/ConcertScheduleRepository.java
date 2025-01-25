package concertreservation.concert.service;

import concertreservation.concert.entity.ConcertSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConcertScheduleRepository {
    List<ConcertSchedule> findByConcertId(Long concertId);
    Optional<ConcertSchedule> findById(Long concertScheduleId);
    ConcertSchedule save(ConcertSchedule concertSchedule);
    boolean existsByConcertIdAndConcertDate(Long concertId, LocalDate concertDate);
}
