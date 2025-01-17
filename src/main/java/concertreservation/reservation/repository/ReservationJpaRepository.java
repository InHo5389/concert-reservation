package concertreservation.reservation.repository;

import concertreservation.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {
    Optional<Reservation> findBySeatId(Long seatId);
}
