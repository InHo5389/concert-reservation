package concertreservation.user.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointChargeRequest {
    private Long userId;
    private int point;
}
