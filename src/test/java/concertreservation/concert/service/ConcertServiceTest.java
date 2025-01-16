package concertreservation.concert.service;

import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @Test
    @DisplayName("콘서트 스케줄이 2025년 1월 1일,1월 2일 2개일때 응답 size가 총 2개여야 한다.")
    void availableDate() {
        //given
        Long concertId = 1L;
        LocalDate firstDate = LocalDate.of(2025, 1, 1);
        LocalDate secondDate = LocalDate.of(2025, 1, 2);
        List<ConcertSchedule> concertSchedules = List.of(
                new ConcertSchedule(1L, concertId, firstDate),
                new ConcertSchedule(2L, concertId, secondDate)
        );
        given(concertScheduleRepository.findByConcertId(concertId))
                .willReturn(concertSchedules);
        //when
        ConcertAvailableDateResponse response = concertService.availableDate(concertId);
        //then
        Assertions.assertThat(response.getAvailableDates().size()).isEqualTo(2);
        Assertions.assertThat(response.getAvailableDates().get(0).getConcertDate()).isEqualTo(firstDate);
        Assertions.assertThat(response.getAvailableDates().get(1).getConcertDate()).isEqualTo(secondDate);
    }
}