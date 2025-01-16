package concertreservation.concert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String name;
    private String imageUrl;

    public static Concert create(String title, String name, String imageUrl) {
        Concert concert = new Concert();
        concert.title = title;
        concert.name = name;
        concert.imageUrl = imageUrl;
        return concert;
    }
}
