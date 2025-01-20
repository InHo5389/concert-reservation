package concertreservation.reservation.service;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.ConcertRepository;
import concertreservation.concert.service.ConcertScheduleRepository;
import concertreservation.concert.service.SeatRepository;
import concertreservation.reservation.entity.PaymentHistory;
import concertreservation.reservation.entity.Reservation;
import concertreservation.reservation.entity.ReservationStatus;
import concertreservation.reservation.service.response.PaymentResponse;
import concertreservation.reservation.service.response.ReservationResponse;
import concertreservation.user.service.PointHistoryRepository;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.PointStatus;
import concertreservation.user.service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final ConcertScheduleRepository concertScheduleRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    public ReservationResponse reservation(Long userId, Long concertScheduleId, Long seatId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        ConcertSchedule concertSchedule = concertScheduleRepository.findById(concertScheduleId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SCHEDULE));
        Concert concert = concertRepository.findById(concertSchedule.getConcertId())
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT));
        Seat seat = seatRepository.findByIdWithOptimisticLock(seatId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SEAT));

        if (!seat.getConcertScheduleId().equals(concertScheduleId)) {
            throw new CustomGlobalException(ErrorType.NOT_FOUNT_CONCERT_SCHEDULE_SEAT);
        }

        if (!seat.isAvailableSeat()) {
            throw new CustomGlobalException(ErrorType.ALREADY_RESERVED_SEAT);
        }

        Reservation reservation = Reservation.create(userId, seatId, seat.getSeatPrice(), concert.getTitle(), seat.getSeatNumber());
        reservationRepository.save(reservation);
        seat.updateSeatStatus(SeatStatus.RESERVED);

        return ReservationResponse.from(reservation, concert, concertSchedule, user, seat);
    }

    @Transactional
    public ReservationResponse reservationPessimistic(Long userId, Long concertScheduleId, Long seatId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        ConcertSchedule concertSchedule = concertScheduleRepository.findById(concertScheduleId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SCHEDULE));
        Concert concert = concertRepository.findById(concertSchedule.getConcertId())
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT));
        Seat seat = seatRepository.findByIdWithPessimistic(seatId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SEAT));

        if (!seat.getConcertScheduleId().equals(concertScheduleId)) {
            throw new CustomGlobalException(ErrorType.NOT_FOUNT_CONCERT_SCHEDULE_SEAT);
        }

        if (!seat.isAvailableSeat()) {
            throw new CustomGlobalException(ErrorType.ALREADY_RESERVED_SEAT);
        }

        Reservation reservation = Reservation.create(userId, seatId, seat.getSeatPrice(), concert.getTitle(), seat.getSeatNumber());
        reservationRepository.save(reservation);
        seat.updateSeatStatus(SeatStatus.RESERVED);

        return ReservationResponse.from(reservation, concert, concertSchedule, user, seat);
    }

    @Transactional
    public PaymentResponse payment(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdWithPessimisticLock(reservationId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_RESERVATION));
        reservation.validReservation();
        reservation.changeReservationStatus(ReservationStatus.PAID);

        User user = userRepository.findByIdWithPessimisticLock(reservation.getUserId())
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        user.decreasePoint(reservation.getReservationPoint());

        pointHistoryRepository.save(PointHistory.create(user.getId(), reservation.getReservationPoint(), PointStatus.USE));
        PaymentHistory paymentHistory = paymentHistoryRepository.save(PaymentHistory.create(reservationId, reservation.getReservationPoint()));

        return PaymentResponse.from(paymentHistory, reservation);
    }
}
