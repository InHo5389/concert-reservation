package concertreservation.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_histories")
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reservationId;
    private int paymentPoint;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PaymentHistory create(Long reservationId, int paymentPoint) {
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.reservationId = reservationId;
        paymentHistory.paymentPoint = paymentPoint;
        paymentHistory.paymentStatus = PaymentStatus.COMPLETED;
        LocalDateTime now = LocalDateTime.now();
        paymentHistory.createdAt = now;
        paymentHistory.modifiedAt = now;
        return paymentHistory;
    }
}
