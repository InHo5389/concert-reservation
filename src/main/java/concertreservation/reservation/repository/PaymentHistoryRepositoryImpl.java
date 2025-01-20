package concertreservation.reservation.repository;

import concertreservation.reservation.entity.PaymentHistory;
import concertreservation.reservation.service.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {

    private final PaymentHistoryJpaRepository paymentHistoryJpaRepository;

    @Override
    public PaymentHistory save(PaymentHistory paymentHistory) {
        return paymentHistoryJpaRepository.save(paymentHistory);
    }

    @Override
    public Optional<PaymentHistory> findByReservationId(Long reservationId) {
        return paymentHistoryJpaRepository.findByReservationId(reservationId);
    }
}
