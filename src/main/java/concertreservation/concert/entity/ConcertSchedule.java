package concertreservation.concert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "concert_schedules")
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertId;
    private LocalDate concertDate;
    private String startTime;
    private String endTime;

    public static ConcertSchedule create(Long concertId, LocalDate concertDate, String startTime, String endTime) {
        ConcertSchedule concertSchedule = new ConcertSchedule();
        concertSchedule.concertId = concertId;
        concertSchedule.concertDate = concertDate;
        concertSchedule.startTime = startTime;
        concertSchedule.endTime = endTime;
        return concertSchedule;
    }
}
