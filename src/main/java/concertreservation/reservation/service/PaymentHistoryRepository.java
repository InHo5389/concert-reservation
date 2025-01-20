package concertreservation.reservation.service;

import concertreservation.reservation.entity.PaymentHistory;

import java.util.Optional;

public interface PaymentHistoryRepository {
    PaymentHistory save(PaymentHistory paymentHistory);
    Optional<PaymentHistory> findByReservationId(Long reservationId);
}
