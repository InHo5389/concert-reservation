package concertreservation.user.facade;

import concertreservation.user.service.UserService;
import concertreservation.user.service.response.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public UserPointResponse chargePointOptimisticLock(Long userId, int point) {
        while (true){
            try {
                return userService.chargePointOptimisticLock(userId, point);
            }catch (Exception e){

            }
        }
    }
}
