package concertreservation.user.service.entity;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private int point;

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

    public static User create(String name, String phoneNumber, int point) {
        User user = new User();
        user.name = name;
        user.phoneNumber = phoneNumber;
        user.point = point;
        return user;
    }

    public void chargePoint(int point) {
        this.point += point;
    }

    public void decreasePoint(int point) {
        if (this.point < point) {
            throw new CustomGlobalException(ErrorType.NOT_ENOUGH_POINT);
        }
        this.point -= point;
    }
}
