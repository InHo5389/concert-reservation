package concertreservation.user.service;

import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.PointStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointHistoryUpdaterTest {

    @Autowired
    private PointHistoryUpdater pointHistoryUpdater;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Test
    @DisplayName("포인트를 충전하면 charge type으로 포인트 히스토리에 저장된다.")
    void savePointHistory_charge(){
        //given
        //when
        PointHistory pointHistory = pointHistoryUpdater.savePointHistory(1L, 500, PointStatus.CHARGE);
        PointHistory savedPointHistory = pointHistoryRepository.findById(pointHistory.getId()).orElseThrow();
        //then
        Assertions.assertThat(savedPointHistory.getStatus()).isEqualByComparingTo(PointStatus.CHARGE);
    }

    @Test
    @DisplayName("포인트를 사용하면 use type으로 포인트 히스토리에 저장된다.")
    void savePointHistory_use(){
        //given
        //when
        PointHistory pointHistory = pointHistoryUpdater.savePointHistory(1L, 500, PointStatus.USE);
        PointHistory savedPointHistory = pointHistoryRepository.findById(pointHistory.getId()).orElseThrow();
        //then
        Assertions.assertThat(savedPointHistory.getStatus()).isEqualByComparingTo(PointStatus.USE);
    }
}