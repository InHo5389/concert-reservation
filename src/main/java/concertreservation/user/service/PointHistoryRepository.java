package concertreservation.user.service;

import concertreservation.user.service.entity.PointHistory;

import java.util.Optional;

public interface PointHistoryRepository {

    PointHistory save(PointHistory pointHistory);
    Optional<PointHistory> findByUserId(Long id);
    Optional<PointHistory> findById(Long id);
}
