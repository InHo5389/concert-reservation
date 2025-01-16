package concertreservation.concert.service;

import concertreservation.concert.entity.Concert;

import java.util.Optional;

public interface ConcertRepository {

    Optional<Concert> findById(Long concertId);
}
