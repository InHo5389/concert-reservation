package concertreservation.reservation.service.component;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.reservation.entity.PaymentHistory;
import concertreservation.reservation.entity.Reservation;
import concertreservation.reservation.entity.ReservationStatus;
import concertreservation.reservation.service.PaymentHistoryRepository;
import concertreservation.reservation.service.response.PaymentResponse;
import concertreservation.user.service.PointHistoryRepository;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.PointStatus;
import concertreservation.user.service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessor {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentResponse processPayment(Reservation reservation) {
        reservation.validReservation();
        reservation.changeReservationStatus(ReservationStatus.PAID);

        User user = processUserPayment(reservation);
        PaymentHistory paymentHistory = createPaymentHistory(reservation);
        createPointHistory(user, reservation);

        return PaymentResponse.from(paymentHistory, reservation);
    }

    private User processUserPayment(Reservation reservation) {
        User user = userRepository.findByIdWithPessimisticLock(reservation.getUserId())
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        user.decreasePoint(reservation.getReservationPoint());
        return user;
    }

    private PaymentHistory createPaymentHistory(Reservation reservation) {
        return paymentHistoryRepository.save(
                PaymentHistory.create(reservation.getId(), reservation.getReservationPoint())
        );
    }

    private void createPointHistory(User user, Reservation reservation) {
        pointHistoryRepository.save(
                PointHistory.create(user.getId(), reservation.getReservationPoint(), PointStatus.USE)
        );
    }
}
