package concertreservation.reservation.service;

import concertreservation.reservation.entity.PaymentHistory;
import concertreservation.reservation.entity.PaymentStatus;
import concertreservation.reservation.entity.Reservation;
import concertreservation.reservation.service.response.PaymentResponse;
import concertreservation.user.service.PointHistoryRepository;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.PointStatus;
import concertreservation.user.service.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ReservationIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("결제 시 포인트 히스토리, 결제 히스토리가 같이 저장되어야 한다.")
    void savePaymentHistoryAndPointHistoryWhenPaymentSuccess() {
        //given
        User user = User.create("이름1", "번호1", 50000);
        User savedUser = userRepository.save(user);

        Reservation reservation = Reservation.create(user.getId(), 1L, 5000, "제목1", "A1");
        Reservation savedReservation = reservationRepository.save(reservation);

        //when
        PaymentResponse response = reservationService.payment(savedReservation.getId());

        PaymentHistory paymentHistory = paymentHistoryRepository.findByReservationId(savedReservation.getId()).get();
        PointHistory pointHistory = pointHistoryRepository.findByUserId(savedUser.getId()).get();
        //then
        assertThat(paymentHistory.getPaymentStatus()).isEqualByComparingTo(PaymentStatus.COMPLETED);
        assertThat(paymentHistory.getPaymentPoint()).isEqualTo(savedReservation.getReservationPoint());

        assertThat(pointHistory.getUsePoint()).isEqualTo(savedReservation.getReservationPoint());
        assertThat(pointHistory.getStatus()).isEqualByComparingTo(PointStatus.USE);

        assertThat(response)
                .extracting("paymentId", "concertTitle", "seatNumber", "paymentPoint")
                .containsExactlyInAnyOrder(
                        paymentHistory.getId(), savedReservation.getConcertTitle(), savedReservation.getSeatNumber(),
                        savedReservation.getReservationPoint());
    }
}
