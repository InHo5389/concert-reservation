package concertreservation.user.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point_histories")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int usePoint;

    @Enumerated(EnumType.STRING)
    private PointStatus status;
    private LocalDateTime createAt;

    public PointHistory(Long userId, int usePoint, PointStatus status, LocalDateTime createAt) {
        this.userId = userId;
        this.usePoint = usePoint;
        this.status = status;
        this.createAt = createAt;
    }

    public static PointHistory create(Long userId, int usePoint, PointStatus status) {
        PointHistory pointHistory = new PointHistory();
        pointHistory.userId = userId;
        pointHistory.usePoint = usePoint;
        pointHistory.status = status;
        pointHistory.createAt = LocalDateTime.now();
        return pointHistory;
    }
}
