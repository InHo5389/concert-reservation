package concertreservation.concert.service.response;

import concertreservation.concert.entity.ConcertSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertAvailableDateResponse {

    private List<ConcertDateInfo> availableDates;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConcertDateInfo {
        private Long concertId;
        private LocalDate concertDate;
    }

    public static ConcertAvailableDateResponse from(List<ConcertSchedule> concertSchedules) {
        List<ConcertDateInfo> concertDateInfos = concertSchedules.stream()
                .map(concertSchedule -> new ConcertDateInfo(
                        concertSchedule.getConcertId(),
                        concertSchedule.getConcertDate()
                ))
                .collect(Collectors.toList());
        return new ConcertAvailableDateResponse(concertDateInfos);
    }
}
