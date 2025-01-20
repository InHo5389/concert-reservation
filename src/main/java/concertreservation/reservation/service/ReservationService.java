package concertreservation.reservation.service;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.reservation.entity.Reservation;
import concertreservation.reservation.service.component.PaymentProcessor;
import concertreservation.reservation.service.component.ReservationProcessor;
import concertreservation.reservation.service.component.dto.ReservationContext;
import concertreservation.reservation.service.response.PaymentResponse;
import concertreservation.reservation.service.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final PaymentProcessor paymentProcessor;
    private final ReservationProcessor reservationProcessor;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse reservation(Long userId, Long concertScheduleId, Long seatId) {
        ReservationContext context = reservationProcessor.prepare(userId, concertScheduleId, seatId);
        Reservation reservation = saveReservation(context);

        return ReservationResponse.from(reservation, context.getConcert(),
                context.getConcertSchedule(), context.getUser(), context.getSeat());
    }

    @Transactional
    public ReservationResponse reservationPessimistic(Long userId, Long concertScheduleId, Long seatId) {
        ReservationContext context = reservationProcessor.prepare(userId, concertScheduleId, seatId);
        Reservation reservation = saveReservation(context);

        return ReservationResponse.from(reservation, context.getConcert(),
                context.getConcertSchedule(), context.getUser(), context.getSeat());
    }

    @Transactional
    public PaymentResponse payment(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdWithPessimisticLock(reservationId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_RESERVATION));

        return paymentProcessor.processPayment(reservation);
    }

    private Reservation saveReservation(ReservationContext context) {
        Reservation reservation = Reservation.create(
                context.getUser().getId(),
                context.getSeat().getId(),
                context.getSeat().getSeatPrice(),
                context.getConcert().getTitle(),
                context.getSeat().getSeatNumber()
        );
        return reservationRepository.save(reservation);
    }
}
