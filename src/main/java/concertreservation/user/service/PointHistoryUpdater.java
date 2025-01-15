package concertreservation.user.service;

import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.PointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointHistoryUpdater {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistory savePointHistory(Long userId,int point, PointStatus status){
        PointHistory pointHistory = PointHistory.create(userId, point, status);
        return pointHistoryRepository.save(pointHistory);
    }
}
