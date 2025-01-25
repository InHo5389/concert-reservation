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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final SeatRepository seatRepository;
    private final ConcertRepository concertRepository;
    private final ConcertRedisRepository concertRedisRepository;
    private final ConcertScheduleRepository concertScheduleRepository;

    public ConcertListResponse concerts(Long pageSize, Long lastConcertId) {
        List<Concert> redisConcerts = concertRedisRepository.readAllInfiniteScroll(lastConcertId, pageSize);
        if (redisConcerts.size() == pageSize) {
            return ConcertListResponse.from(redisConcerts);
        }

        List<Concert> concerts = lastConcertId == null ?
                concertRepository.findAllInfiniteScroll(pageSize) :
                concertRepository.findAllInfiniteScroll(pageSize, lastConcertId);

        concertRedisRepository.add(concerts, lastConcertId, pageSize);

        return ConcertListResponse.from(concerts);
    }

    @Cacheable(value = "concert::schedule",key = "#concertId")
    public ConcertAvailableDateResponse availableDate(Long concertId) {
        concertRepository.findById(concertId).orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT));

        List<ConcertSchedule> concertSchedules = concertScheduleRepository.findByConcertId(concertId);
        return ConcertAvailableDateResponse.from(concertSchedules);
    }

    @CacheEvict(value = "concert::schedule", key = "#concertId")
    public void updateSchedule(Long concertId, LocalDate scheduleDate) {
        concertRepository.findById(concertId).orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT));
        if (concertScheduleRepository.existsByConcertIdAndConcertDate(concertId, scheduleDate)) {
            throw new CustomGlobalException(ErrorType.ALREADY_EXIST_SCHEDULE);
        }

        ConcertSchedule schedule = ConcertSchedule.create(concertId, scheduleDate);
        concertScheduleRepository.save(schedule);
    }

    @Cacheable(value = "concert::seat",key = "#concertScheduleId")
    public ConcertAvailableSeatResponse availableSeat(Long concertScheduleId) {
        concertScheduleRepository.findById(concertScheduleId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_CONCERT_SCHEDULE));

        List<Seat> seats = seatRepository.findByConcertScheduleIdAndSeatStatus(concertScheduleId, SeatStatus.AVAILABLE);
        return ConcertAvailableSeatResponse.from(seats);
    }
}
