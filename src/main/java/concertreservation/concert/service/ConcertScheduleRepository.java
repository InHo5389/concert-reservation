package concertreservation.concert.service;

import concertreservation.concert.entity.ConcertSchedule;

import java.util.List;

public interface ConcertScheduleRepository {
    List<ConcertSchedule> findByConcertId(Long concertId);
}
