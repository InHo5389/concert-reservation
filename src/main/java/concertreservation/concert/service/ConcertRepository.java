package concertreservation.concert.service;

import concertreservation.concert.entity.Concert;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {

    Optional<Concert> findById(Long concertId);
    Concert save(Concert concert);
    List<Concert> findAll();
    List<Concert> findAllInfiniteScroll(Long limit);
    List<Concert> findAllInfiniteScroll(Long limit, Long lastConcertId);
}
