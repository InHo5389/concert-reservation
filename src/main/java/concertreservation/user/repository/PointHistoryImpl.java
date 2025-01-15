package concertreservation.user.repository;

import concertreservation.user.service.PointHistoryRepository;
import concertreservation.user.service.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointHistoryImpl implements PointHistoryRepository {

    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryJpaRepository.save(pointHistory);
    }

    @Override
    public Optional<PointHistory> findById(Long id) {
        return pointHistoryJpaRepository.findById(id);
    }
}
