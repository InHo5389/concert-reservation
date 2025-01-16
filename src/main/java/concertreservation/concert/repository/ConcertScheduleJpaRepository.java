package concertreservation.concert.repository;

import concertreservation.concert.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule,Long> {
}
