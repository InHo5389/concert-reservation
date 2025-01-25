package concertreservation.concert.controller.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AddScheduleRequest {

    private LocalDate scheduleDate;
}
