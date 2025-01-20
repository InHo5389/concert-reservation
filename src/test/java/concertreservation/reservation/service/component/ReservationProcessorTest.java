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
import concertreservation.reservation.entity.ReservationStatus;
import concertreservation.reservation.service.ReservationRepository;
import concertreservation.reservation.service.component.dto.ReservationContext;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReservationProcessorTest {

    @InjectMocks
    private ReservationProcessor reservationProcessor;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ConcertRepository concertRepository;
    @Mock
    private ConcertScheduleRepository concertScheduleRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private SeatRepository seatRepository;

    @Test
    @DisplayName("콘서트 예약시 좌석에 대한 콘서트 스케줄이 다르면 예외가 발생한다. ")
    void prepareFailNotEqualConcertSchedule() {
        //given
        long userId = 1L;
        long concertId = 1L;
        long seatId = 1L;
        long concertScheduleId = 1L;
        long invalidScheduleId = 2L;

        given(userRepository.findById(userId))
                .willReturn(Optional.of(new User(userId, "이름1", "번호1", 50000)));
        given(concertScheduleRepository.findById(invalidScheduleId))
                .willReturn(Optional.of(new ConcertSchedule(invalidScheduleId, concertId, LocalDate.of(2025, 1, 15))));
        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(new Concert(concertId, "콘서트1", "아이유", "https://")));
        given(seatRepository.findByIdWithOptimisticLock(seatId))
                .willReturn(Optional.of(new Seat(seatId, concertScheduleId, "A1", SeatStatus.RESERVED, 50000, null)));
        //when
        //then
        assertThatThrownBy(
                () -> reservationProcessor.prepare(userId, invalidScheduleId, seatId)
        )
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage(ErrorType.NOT_FOUND_CONCERT_SCHEDULE_SEAT.getMessage());
    }

    @Test
    @DisplayName("콘서트 예약시 좌석이 이미 예약 되어 있으면 예외가 발생한다.")
    void prepareFailAlreadyReserved(){
        //given
        long userId = 1L;
        long concertId = 1L;
        long seatId = 1L;
        long concertScheduleId = 1L;

        given(userRepository.findById(userId))
                .willReturn(Optional.of(new User(userId, "이름1", "번호1", 50000)));
        given(concertScheduleRepository.findById(concertScheduleId))
                .willReturn(Optional.of(new ConcertSchedule(concertScheduleId, concertId, LocalDate.of(2025, 1, 15))));
        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(new Concert(concertId, "콘서트1", "아이유", "https://")));
        given(seatRepository.findByIdWithOptimisticLock(seatId))
                .willReturn(Optional.of(new Seat(seatId, concertScheduleId, "A1", SeatStatus.RESERVED, 50000, null)));
        //when
        //then
        assertThatThrownBy(
                () -> reservationProcessor.prepare(userId, concertScheduleId, seatId)
        )
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage(ErrorType.ALREADY_RESERVED_SEAT.getMessage());
    }

    @Test
    @DisplayName("좌석 예약시 성공하면 좌석 상태가 RESERVED가 되어야 한다.")
    void reservationSuccess(){
        //given
        long userId = 1L;
        long concertId = 1L;
        long seatId = 1L;
        long concertScheduleId = 1L;

        given(userRepository.findById(userId))
                .willReturn(Optional.of(new User(userId, "이름1", "번호1", 50000)));
        given(concertScheduleRepository.findById(concertScheduleId))
                .willReturn(Optional.of(new ConcertSchedule(concertScheduleId, concertId, LocalDate.of(2025, 1, 15))));
        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(new Concert(concertId, "콘서트1", "아이유", "https://")));
        given(seatRepository.findByIdWithOptimisticLock(seatId))
                .willReturn(Optional.of(new Seat(seatId, concertScheduleId, "A1", SeatStatus.AVAILABLE, 50000, null)));
        //when
        ReservationContext reservationContext = reservationProcessor.prepare(userId, concertScheduleId, seatId);
        //then
        reservationContext.getSeat().getSeatStatus().getContent();
        assertThat(reservationContext.getSeat().getSeatStatus())
                .isEqualByComparingTo(SeatStatus.RESERVED);
    }
}