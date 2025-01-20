package concertreservation.concert.service;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import concertreservation.concert.service.response.ConcertAvailableSeatResponse;
import concertreservation.concert.service.response.ConcertListResponse;
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

    @Test
    @DisplayName("pageSize가 10이고 콘서트가 4개 저장되어 있다면 첫 페이지 콘서트 목록은 4개가 조회한다.")
    void concertLists(){
        //given
        Long pageSize = 10L;
        List<Concert> concerts = List.of(
                new Concert("콘서트1", "가수1", "이미지1"),
                new Concert("콘서트2", "가수2", "이미지2"),
                new Concert("콘서트3", "가수3", "이미지3"),
                new Concert("콘서트4", "가수4", "이미지4")
        );

        given(concertRepository.findAllInfiniteScroll(pageSize))
                .willReturn(concerts);
        //when
        ConcertListResponse response = concertService.concerts(pageSize, null);
        //then
        assertThat(response.getConcertInfos()).hasSize(4);
        assertThat(response.getConcertInfos().get(0)).extracting(
                "title","name","imageUrl"
        ).containsExactlyInAnyOrder("콘서트1", "가수1", "이미지1");
        assertThat(response.getConcertInfos().get(1)).extracting(
                "title","name","imageUrl"
        ).containsExactlyInAnyOrder("콘서트2", "가수2", "이미지2");
    }

    @Test
    @DisplayName("pageSize 2,3,5 별 콘서트 목록 조회하면 pageSize별로 조회되어야 한다.")
    void getConcertListsByPageSize(){
        //given
        List<Concert> allConcerts = List.of(
                new Concert("콘서트1", "가수1", "이미지1"),
                new Concert("콘서트2", "가수2", "이미지2"),
                new Concert("콘서트3", "가수3", "이미지3"),
                new Concert("콘서트4", "가수4", "이미지4"),
                new Concert("콘서트5", "가수5", "이미지5")
        );

        given(concertRepository.findAllInfiniteScroll(2L))
                .willReturn(allConcerts.subList(0, 2));

        given(concertRepository.findAllInfiniteScroll(3L))
                .willReturn(allConcerts.subList(0, 3));

        given(concertRepository.findAllInfiniteScroll(5L))
                .willReturn(allConcerts);

        //when & then
        ConcertListResponse response2 = concertService.concerts(2L, null);
        assertThat(response2.getConcertInfos()).hasSize(2);
        assertThat(response2.getConcertInfos())
                .extracting("title")
                .containsExactly("콘서트1", "콘서트2");

        ConcertListResponse response3 = concertService.concerts(3L, null);
        assertThat(response3.getConcertInfos()).hasSize(3);
        assertThat(response3.getConcertInfos())
                .extracting("title")
                .containsExactly("콘서트1", "콘서트2", "콘서트3");

        ConcertListResponse response5 = concertService.concerts(5L, null);
        assertThat(response5.getConcertInfos()).hasSize(5);
        assertThat(response5.getConcertInfos())
                .extracting("title")
                .containsExactly("콘서트1", "콘서트2", "콘서트3", "콘서트4", "콘서트5");
    }

    @Test
    @DisplayName("lastConcertId를 사용한 다음 페이지 콘서트 목록 조회")
    void getNextPageConcertList() {
        //given
        Long pageSize = 10L;
        Long lastConcertId = 2L;

        List<Concert> concerts = List.of(
                new Concert(1L, "콘서트1", "가수1", "이미지1"),
                new Concert(2L, "콘서트2", "가수2", "이미지2"),
                new Concert(3L, "콘서트3", "가수3", "이미지3"),
                new Concert(4L, "콘서트4", "가수4", "이미지4")
        );

        given(concertRepository.findAllInfiniteScroll(pageSize, 2L))
                .willReturn(concerts.subList(2, 4));

        //when
        ConcertListResponse response = concertService.concerts(pageSize, lastConcertId);

        //then
        assertThat(response.getConcertInfos()).hasSize(2);
        assertThat(response.getConcertInfos())
                .extracting("id", "title")
                .containsExactly(
                        tuple(3L, "콘서트3"),
                        tuple(4L, "콘서트4")
                );
    }
}