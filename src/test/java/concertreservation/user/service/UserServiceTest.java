package concertreservation.user.service;

import concertreservation.common.exception.CustomGlobalException;
import concertreservation.user.service.entity.PointHistory;
import concertreservation.user.service.entity.PointStatus;
import concertreservation.user.service.entity.User;
import concertreservation.user.service.response.UserPointReadResponse;
import concertreservation.user.service.response.UserPointUpdateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PointHistoryUpdater pointHistoryUpdater;

    @Test
    @DisplayName("기존 포인트가 100원있고 500원 충전하면 600원이 되어야 한다.")
    void chargePoint() {
        //given
        User user = new User(1L, "이름1", "01012345678", 100);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        int chargePoint = 500;
        //when
        UserPointUpdateResponse response = userService.chargePoint(user.getId(), chargePoint);
        //then
        assertThat(user.getPoint()).isEqualTo(600);
        assertThat(response).extracting("name", "point")
                .containsExactlyInAnyOrder("이름1", 600);
    }

    @Test
    @DisplayName("기존 포인트가 1000원있고 500원 결제하면 500원이 되어야 한다.")
    void decreasePoint() {
        //given
        User user = new User(1L, "이름1", "01012345678", 1000);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        int decreasePoint = 500;
        //when
        UserPointUpdateResponse response = userService.decreasePoint(user.getId(), decreasePoint);
        //then
        assertThat(user.getPoint()).isEqualTo(500);
        assertThat(response).extracting("name", "point")
                .containsExactlyInAnyOrder("이름1", 500);
    }

    @Test
    @DisplayName("포인트 충전시 포인트 히스토리가 함께 저장된다")
    void savePointHistoryWhenChargingPoints() {
        //given
        User user = new User(1L, "이름1", "01012345678", 100);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        int chargePoint = 500;
        given(pointHistoryUpdater.savePointHistory(any(), any(), any()))
                .willReturn(new PointHistory(1L, user.getId(), chargePoint, PointStatus.CHARGE, LocalDateTime.now()));
        //when
        UserPointUpdateResponse response = userService.chargePointPessimisticLock(user.getId(), chargePoint);
        //then
        verify(pointHistoryUpdater, times(1))
                .savePointHistory(user.getId(), chargePoint, PointStatus.CHARGE);
        assertThat(user.getPoint()).isEqualTo(600);
    }

    @Test
    @DisplayName("회원이 없을 경우 CustomGlobalException이 발생한다.")
    void readPointWithNotFountUser() {
        //given
        //when
        //then
        assertThatThrownBy(() -> userService.readPoint(1L))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessage("회원을 찾을수 없습니다.");
    }
}