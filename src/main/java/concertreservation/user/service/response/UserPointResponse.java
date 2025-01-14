package concertreservation.user.service.response;

import concertreservation.user.service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPointResponse {

    private Long userId;
    private String name;
    private int point;

    public static UserPointResponse from(User user) {
        UserPointResponse response = new UserPointResponse();
        response.userId = user.getId();
        response.name = user.getName();
        response.point = user.getPoint();
        return response;
    }
}
