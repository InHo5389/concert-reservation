package concertreservation.reservation.service.component;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.ConcertRepository;
import concertreservation.concert.service.ConcertScheduleRepository;
import concertreservation.concert.service.SeatRepository;
import concertreservation.reservation.service.component.dto.ReservationContext;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationProcessor {

    private final UserRepository userRepository;
    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    public ReservationContext prepare(Long userId, Long concertScheduleId, Long seatId) {
        User user = findUser(userId);
        ConcertSchedule schedule = findConcertSchedule(concertScheduleId);
        Concert concert = findConcert(schedule.getConcertId());
        Seat seat = findAndValidateSeat(seatId, concertScheduleId);

        seat.updateSeatStatus(SeatStatus.RESERVED);

        return new ReservationContext(user, concert, schedule, seat);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
    }

    private ConcertSchedule findConcertSchedule(Long scheduleId) {
        return concertScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SCHEDULE));
    }

    private Concert findConcert(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT));
    }

    private Seat findAndValidateSeat(Long seatId, Long scheduleId) {
        Seat seat = seatRepository.findByIdWithOptimisticLock(seatId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SEAT));
        seat.validateSeatAvailability(scheduleId);
        return seat;
    }
}