package concertreservation.user.service;

import concertreservation.user.service.entity.User;
import concertreservation.user.service.response.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserPointResponse chargePoint(Long userId, int point) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원이 없습니다."));
        user.chargePoint(point);
        return UserPointResponse.from(user);
    }

    @Transactional
    public UserPointResponse chargePointPessimisticLock(Long userId, int point) {
        User user = userRepository.findByIdWithPessimisticLock(userId).orElseThrow(() -> new RuntimeException("회원이 없습니다."));
        user.chargePoint(point);
        return UserPointResponse.from(user);
    }

    @Transactional
    public UserPointResponse chargePointOptimisticLock(Long userId, int point) {

        User user = userRepository.findByIdWithOptimisticLock(userId)
                .orElseThrow(() -> new RuntimeException("회원이 없습니다."));
        user.chargePoint(point);
        return UserPointResponse.from(user);
    }

    @Transactional
    public UserPointResponse decreasePoint(Long userId, int point) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원이 없습니다."));
        user.decreasePoint(point);
        return UserPointResponse.from(user);
    }

    public UserPointResponse readPoint(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원이 없습니다."));
        return UserPointResponse.from(user);
    }
}
