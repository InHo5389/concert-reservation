package concertreservation.user.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private int point;

//    @Version
    private Long version;

    public User(String name, String phoneNumber, int point) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.point = point;
    }

    public User(Long id, String name, String phoneNumber, int point) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.point = point;
    }

    public static void create(String name, String phoneNumber, int point) {
        User user = new User();
        user.name = name;
        user.phoneNumber = phoneNumber;
        user.point = point;
    }

    public void chargePoint(int point) {
        this.point += point;
    }

    public void decreasePoint(int point) {
        this.point -= point;
    }
}
