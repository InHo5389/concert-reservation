package concertreservation.reservation.entity;

import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.reservation.service.response.ReservationResponse;
import concertreservation.user.service.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long seatId;
    private String concertTitle;
    private String seatNumber;
    private int reservationPoint;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Reservation create(Long userId, Long seatId, int reservationPoint, String concertTitle, String seatNumber) {
        LocalDateTime now = LocalDateTime.now();

        Reservation reservation = new Reservation();
        reservation.userId = userId;
        reservation.seatId = seatId;
        reservation.concertTitle = concertTitle;
        reservation.seatNumber = seatNumber;
        reservation.reservationPoint = reservationPoint;
        reservation.reservationStatus = ReservationStatus.RESERVED;
        reservation.expiredAt = now.plusMinutes(5);
        reservation.createdAt = now;
        reservation.modifiedAt = now;
        return reservation;
    }
}
