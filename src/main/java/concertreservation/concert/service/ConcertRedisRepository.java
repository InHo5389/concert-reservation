package concertreservation.concert.service;

import concertreservation.concert.entity.Concert;

import java.util.List;

public interface ConcertRedisRepository {
    List<Concert> readAllInfiniteScroll(Long lastConcertId, Long limit);
    void add(List<Concert> concerts, Long lastConcertId, Long limit);
}
