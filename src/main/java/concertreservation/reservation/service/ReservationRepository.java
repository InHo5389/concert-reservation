package concertreservation.reservation.service;

import concertreservation.reservation.entity.Reservation;

import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findBySeatId(Long seatId);
}
