package concertreservation.user.service.response;

import concertreservation.user.service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPointReadResponse {

    private Long userId;
    private String name;
    private int point;

    public static UserPointReadResponse from(User user) {
        UserPointReadResponse response = new UserPointReadResponse();
        response.userId = user.getId();
        response.name = user.getName();
        response.point = user.getPoint();
        return response;
    }
}
