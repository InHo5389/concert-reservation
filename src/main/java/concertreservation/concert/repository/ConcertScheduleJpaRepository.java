package concertreservation.concert.repository;

import concertreservation.concert.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule,Long> {
    List<ConcertSchedule> findByConcertId(Long concertId);
}
