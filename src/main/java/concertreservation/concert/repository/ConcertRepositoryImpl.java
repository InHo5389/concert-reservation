package concertreservation.concert.repository;

import concertreservation.concert.entity.Concert;
import concertreservation.concert.service.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public Optional<Concert> findById(Long concertId) {
        return concertJpaRepository.findById(concertId);
    }

    @Override
    public Concert save(Concert concert) {
        return concertJpaRepository.save(concert);
    }

    @Override
    public List<Concert> findAll() {
        return concertJpaRepository.findAll();
    }

    @Override
    public List<Concert> findAllInfiniteScroll(Long limit) {
        return concertJpaRepository.findAllInfiniteScroll(limit);
    }

    @Override
    public List<Concert> findAllInfiniteScroll(Long limit, Long lastConcertId) {
        return concertJpaRepository.findAllInfiniteScroll(limit, lastConcertId);
    }
}
