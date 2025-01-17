package concertreservation.reservation.repository;

import concertreservation.reservation.entity.Reservation;
import concertreservation.reservation.service.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> findBySeatId(Long seatId) {
        return reservationJpaRepository.findBySeatId(seatId);
    }
}
