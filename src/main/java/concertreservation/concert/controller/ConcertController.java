package concertreservation.concert.controller;

import concertreservation.concert.service.ConcertService;
import concertreservation.concert.service.response.ConcertAvailableDateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/concerts/{concertId}/schedule")
    public ConcertAvailableDateResponse availableDate(@PathVariable Long concertId) {
        return concertService.availableDate(concertId);
    }
}
