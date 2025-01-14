package concertreservation.user.controller;

import concertreservation.user.controller.request.PointChargeRequest;
import concertreservation.user.service.UserService;
import concertreservation.user.service.response.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}/point")
    public UserPointResponse readPoint(@PathVariable Long userId) {
        return userService.readPoint(userId);
    }

    @PatchMapping("/users/point")
    public UserPointResponse chargePoint(@RequestBody PointChargeRequest request) {
        return userService.chargePointPessimisticLock(request.getUserId(), request.getPoint());
    }
}
