package concertreservation.reservation.repository;

import concertreservation.reservation.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistory, Long> {
    Optional<PaymentHistory> findByReservationId(Long reservationId);
}
