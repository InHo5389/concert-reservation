package concertreservation.user.service.response;

import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPointUpdateResponse {

    private Long userId;
    private String name;
    private int point;
    private String pointStatus;
    private LocalDateTime createdAt;

    public static UserPointUpdateResponse from(User user, PointHistory pointHistory) {
        UserPointUpdateResponse response = new UserPointUpdateResponse();
        response.userId = user.getId();
        response.name = user.getName();
        response.point = user.getPoint();
        response.pointStatus = pointHistory.getStatus().getContent();
        response.createdAt = pointHistory.getCreateAt();
        return response;
    }
}
