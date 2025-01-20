package concertreservation.concert.service;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.concert.entity.Concert;
import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.entity.Seat;
import concertreservation.concert.entity.SeatStatus;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import concertreservation.concert.service.response.ConcertAvailableSeatResponse;
import concertreservation.concert.service.response.ConcertListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;
    private final ConcertScheduleRepository concertScheduleRepository;

    public ConcertListResponse concerts(Long pageSize, Long lastConcertId){
        List<Concert> concerts = lastConcertId == null ?
                concertRepository.findAllInfiniteScroll(pageSize) :
                concertRepository.findAllInfiniteScroll(pageSize, lastConcertId);

        return ConcertListResponse.from(concerts);
    }

    public ConcertAvailableDateResponse availableDate(Long concertId) {
        concertRepository.findById(concertId).orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT));

        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertId(concertId);
        return ConcertAvailableDateResponse.from(concertSchedules);
    }

    public ConcertAvailableSeatResponse availableSeat(Long concertScheduleId) {
        concertScheduleRepository.findById(concertScheduleId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SCHEDULE));

        List<Seat> seats = seatRepository.findByConcertScheduleIdAndSeatStatus(concertScheduleId, SeatStatus.AVAILABLE);
        return ConcertAvailableSeatResponse.from(seats);
    }
}
