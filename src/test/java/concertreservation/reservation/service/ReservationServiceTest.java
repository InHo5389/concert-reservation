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
import concertreservation.reservation.service.response.ReservationResponse;
import concertreservation.user.service.UserRepository;
import concertreservation.user.service.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

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
    @DisplayName("좌석 예약이 완료되고 응답값을 확인한다. ")
    void reservation() {
        //given
        long userId = 1L;
        long concertId = 1L;
        long seatId = 1L;
        long concertScheduleId = 1L;

        given(userRepository.findById(concertId))
                .willReturn(Optional.of(new User(userId, "이름1", "번호1", 50000)));
        given(concertScheduleRepository.findById(concertId))
                .willReturn(Optional.of(new ConcertSchedule(concertScheduleId, LocalDate.of(2025, 1, 15))));
        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(new Concert(concertId, "콘서트1", "아이유", "https://")));
        given(seatRepository.findByIdWithOptimisticLock(seatId))
                .willReturn(Optional.of(new Seat(seatId, concertScheduleId, "A1", SeatStatus.AVAILABLE, 50000, null)));
        //when
        ReservationResponse response = reservationService.reservation(userId, concertScheduleId, seatId);
        //then
        assertThat(response)
                .extracting("title", "concertDate", "singerName")
                .containsExactlyInAnyOrder("콘서트1", LocalDate.of(2025, 1, 15), "아이유");
    }

    @Test
    @DisplayName("좌석을 예약할때 이미 예약된 좌석일때 예외가 발생한다.")
    void reservationFailWith() {
        //given
        long userId = 1L;
        long concertId = 1L;
        long seatId = 1L;
        long concertScheduleId = 1L;

        given(userRepository.findById(concertId))
                .willReturn(Optional.of(new User(userId, "이름1", "번호1", 50000)));
        given(concertScheduleRepository.findById(concertId))
                .willReturn(Optional.of(new ConcertSchedule(concertScheduleId, LocalDate.of(2025, 1, 15))));
        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(new Concert(concertId, "콘서트1", "아이유", "https://")));
        given(seatRepository.findByIdWithOptimisticLock(seatId))
                .willReturn(Optional.of(new Seat(seatId, concertScheduleId, "A1", SeatStatus.RESERVED, 50000, null)));
        //when
        //then
        assertThatThrownBy(() -> reservationService.reservation(userId, concertScheduleId, seatId))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage(ErrorType.ALREADY_RESERVED_SEAT.getMessage());
    }
}