package concertreservation.concert.service;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import concertreservation.concert.service.response.ConcertAvailableSeatResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private SeatRepository seatRepository;

    @Test
    @DisplayName("콘서트 스케줄이 2025년 1월 1일,1월 2일 2개일때 응답 size가 총 2개여야 한다.")
    void availableDate() {
        //given
        Long concertId = 1L;
        Concert concert = new Concert(concertId, "콘서트 제목", "가수 이름", "https://");

        LocalDate firstDate = LocalDate.of(2025, 1, 1);
        LocalDate secondDate = LocalDate.of(2025, 1, 2);
        List<ConcertSchedule> concertSchedules = List.of(
                new ConcertSchedule(1L, concertId, firstDate),
                new ConcertSchedule(2L, concertId, secondDate)
        );

        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(concert));
        given(concertScheduleRepository.findByConcertId(concertId))
                .willReturn(concertSchedules);
        //when
        ConcertAvailableDateResponse response = concertService.availableDate(concertId);
        //then
        assertThat(response.getAvailableDates().size()).isEqualTo(2);
        assertThat(response.getAvailableDates().get(0).getConcertDate()).isEqualTo(firstDate);
        assertThat(response.getAvailableDates().get(1).getConcertDate()).isEqualTo(secondDate);
    }

    @Test
    @DisplayName("콘서트 예약 가능 일자 조회 시 콘서트가 없을시 NOT_FOUND_CONCERT 예외가 발생한다.")
    void availableDate_notFountConcert() {
        //given
        Long concertId = 1L;
        given(concertRepository.findById(concertId))
                .willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> concertService.availableDate(concertId))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage("콘서트를 찾을수 없습니다.");
    }

    @Test
    @DisplayName("이용 가능한 좌석이 5개일때 조회하면 size가 5개 나와야 한다.")
    void availableSeat() {
        //given
        Long concertId = 1L;
        Long concertScheduleId = 1L;

        LocalDate firstDate = LocalDate.of(2025, 1, 1);
        ConcertSchedule concertSchedule = new ConcertSchedule(1L, concertId, firstDate);

        List<Seat> seats = List.of(
                new Seat(1L, concertScheduleId, "1", SeatStatus.AVAILABLE, 50000,null),
                new Seat(2L, concertScheduleId, "10", SeatStatus.AVAILABLE, 50000,null),
                new Seat(3L, concertScheduleId, "15", SeatStatus.AVAILABLE, 50000,null),
                new Seat(4L, concertScheduleId, "19", SeatStatus.AVAILABLE, 50000,null),
                new Seat(5L, concertScheduleId, "20", SeatStatus.AVAILABLE, 50000,null)
        );
        given(concertScheduleRepository.findById(concertScheduleId))
                .willReturn(Optional.of(concertSchedule));
        given(seatRepository.findByConcertScheduleIdAndSeatStatus(concertScheduleId, SeatStatus.AVAILABLE))
                .willReturn(seats);
        //when
        ConcertAvailableSeatResponse response = concertService.availableSeat(concertScheduleId);
        //then
        assertThat(response.getSeatNumber().size()).isEqualTo(5);
        assertThat(response.getSeatNumber())
                .containsOnly("1", "10", "15", "19", "20");
    }
}