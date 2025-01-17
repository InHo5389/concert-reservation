package concertreservation.reservation.service;

import concertreservation.reservation.entity.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
}
