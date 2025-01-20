package concertreservation.reservation.service.component;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.reservation.entity.Reservation;
import concertreservation.reservation.entity.ReservationStatus;
import concertreservation.reservation.service.PaymentHistoryRepository;
import concertreservation.user.service.PointHistoryRepository;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    @InjectMocks
    private PaymentProcessor paymentProcessor;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @Test
    @DisplayName("결제시 예약 상태가 PAID이면 예외가 발생한다.")
    void processPaymentFailAlreadyPaid() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(1L, 1L, 1L, "제목1", "A1", 500, ReservationStatus.PAID, now, now, now);
        //when
        //then
        Assertions.assertThatThrownBy(
                ()->paymentProcessor.processPayment(reservation)
        )
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage(ErrorType.ALREADY_PAID_SEAT.getMessage());
    }

    @Test
    @DisplayName("결제 시 예약 만료 시간이 지나면 예외가 발생한다.")
    void paymentFailWhenAfterExpiredAt() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(1L, 1L, 1L, "제목1", "A1", 500, ReservationStatus.RESERVED, now.minusDays(1), now, now);
        //when
        //then
        Assertions.assertThatThrownBy(
                ()->paymentProcessor.processPayment(reservation)
        )
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage(ErrorType.EXPIRED_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("유저가 포인트가 1000있고 결제할 콘서트 좌석 가격이 10000일 경우 예외가 발생한다.")
    void paymentFailNotEnoughPoint(){
        //given
        LocalDateTime now = LocalDateTime.now();
        long userId = 1L;
        int userPoint = 1000;
        int reservationPoint = 10000;
        Reservation reservation = new Reservation(1L, userId, 1L, "제목1", "A1", reservationPoint, ReservationStatus.RESERVED, now.plusMinutes(1), now, now);
        given(userRepository.findByIdWithPessimisticLock(reservation.getUserId()))
                .willReturn(Optional.of(new User(userId,"이름1","번호1", userPoint)));
        //when
        //then
        assertThatThrownBy(() -> paymentProcessor.processPayment(reservation))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage(ErrorType.NOT_ENOUGH_POINT.getMessage());
    }
}