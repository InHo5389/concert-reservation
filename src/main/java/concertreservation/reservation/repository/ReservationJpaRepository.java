package concertreservation.reservation.repository;

import concertreservation.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {
}
