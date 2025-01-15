package concertreservation.user.repository;

import concertreservation.user.service.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory,Long> {
}
