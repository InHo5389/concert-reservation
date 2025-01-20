package concertreservation.concert.controller;

import concertreservation.concert.service.ConcertService;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import concertreservation.concert.service.response.ConcertAvailableSeatResponse;
import concertreservation.concert.service.response.ConcertListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/concerts")
    public ConcertListResponse concerts(
            @RequestParam("pageSize") Long pageSize,
            @RequestParam(value = "lastConcertId",required = false) Long lastConcertId
    ){
        return concertService.concerts(pageSize,lastConcertId);
    }

    @GetMapping("/concerts/{concertId}/schedule")
    public ConcertAvailableDateResponse availableDate(@PathVariable Long concertId) {
        return concertService.availableDate(concertId);
    }

    @GetMapping("/concerts/seats/{concertScheduleId}")
    public ConcertAvailableSeatResponse availableSeat(@PathVariable Long concertScheduleId) {
        return concertService.availableSeat(concertScheduleId);
    }
}
