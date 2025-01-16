package concertreservation.concert.repository;

import concertreservation.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert,Long> {
}
