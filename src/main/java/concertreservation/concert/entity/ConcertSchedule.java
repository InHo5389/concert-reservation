package concertreservation.concert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    public static ConcertSchedule create(Long concertId, LocalDate concertDate) {
        ConcertSchedule concertSchedule = new ConcertSchedule();
        concertSchedule.concertId = concertId;
        concertSchedule.concertDate = concertDate;
        return concertSchedule;
    }
}
