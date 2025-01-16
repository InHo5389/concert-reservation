package concertreservation.concert.service;

import concertreservation.concert.entity.ConcertSchedule;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import concertreservation.concert.service.response.ConcertAvailableSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final SeatRepository seatRepository;
    private final ConcertScheduleRepository concertScheduleRepository;

    public ConcertAvailableDateResponse availableDate(Long concertId) {
        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertId(concertId);

        return ConcertAvailableDateResponse.from(concertSchedules);
    }
}
