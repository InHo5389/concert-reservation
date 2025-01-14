package concertreservation.user.service;

import concertreservation.user.service.entity.User;
import concertreservation.user.service.response.UserPointResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("기존 포인트가 100원있고 500원 충전하면 600원이 되어야 한다.")
    void chargePoint() {
        //given
        User user = new User(1L, "이름1", "01012345678", 100);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        int chargePoint = 500;
        //when
        UserPointResponse response = userService.chargePoint(user.getId(), chargePoint);
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
        UserPointResponse response = userService.decreasePoint(user.getId(), decreasePoint);
        //then
        assertThat(user.getPoint()).isEqualTo(500);
        assertThat(response).extracting("name", "point")
                .containsExactlyInAnyOrder("이름1", 500);
    }
}