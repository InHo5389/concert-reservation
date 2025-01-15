package concertreservation.user.service;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.PointStatus;
import concertreservation.user.service.entity.User;
import concertreservation.user.service.response.UserPointReadResponse;
import concertreservation.user.service.response.UserPointUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointHistoryUpdater pointHistoryUpdater;

    @Transactional
    public UserPointUpdateResponse chargePoint(Long userId, int point) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        user.chargePoint(point);

        PointHistory pointHistory = pointHistoryUpdater.savePointHistory(userId, point, PointStatus.CHARGE);
        return UserPointUpdateResponse.from(user,pointHistory);
    }

    @Transactional
    public UserPointUpdateResponse chargePointPessimisticLock(Long userId, int point) {
        User user = userRepository.findByIdWithPessimisticLock(userId).orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        user.chargePoint(point);

        PointHistory pointHistory = pointHistoryUpdater.savePointHistory(userId, point, PointStatus.CHARGE);
        return UserPointUpdateResponse.from(user,pointHistory);
    }

    @Transactional
    public UserPointUpdateResponse chargePointOptimisticLock(Long userId, int point) {
        User user = userRepository.findByIdWithOptimisticLock(userId)
                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        user.chargePoint(point);

        PointHistory pointHistory = pointHistoryUpdater.savePointHistory(userId, point, PointStatus.CHARGE);
        return UserPointUpdateResponse.from(user,pointHistory);
    }

    @Transactional
    public UserPointUpdateResponse decreasePoint(Long userId, int point) {
        User user = userRepository.findByIdWithPessimisticLock(userId).orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));
        user.decreasePoint(point);

        PointHistory pointHistory = pointHistoryUpdater.savePointHistory(userId, point, PointStatus.USE);
        return UserPointUpdateResponse.from(user,pointHistory);
    }

    public UserPointReadResponse readPoint(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_USER));

        return UserPointReadResponse.from(user);
    }
}
