package concertreservation.user.repository;

import concertreservation.user.service.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory,Long> {
    Optional<PointHistory> findByUserId(Long id);
}
