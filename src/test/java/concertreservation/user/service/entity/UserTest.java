package concertreservation.user.service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("기존 포인트가 100원있고 500원 충전하면 600원이 되어야 한다.")
    void chargePoint() {
        //given
        int userPoint = 100;
        User user = new User(1L, "이름1", "01012345678", userPoint);
        //when
        int chargePoint = 500;
        user.chargePoint(chargePoint);
        //then
        assertThat(user.getPoint()).isEqualTo(userPoint + chargePoint);
    }

    @Test
    @DisplayName("기존 포인트가 1000원있고 500원 결제하면 500원이 되어야 한다.")
    void decreasePoint() {
        //given
        int userPoint = 1000;
        User user = new User(1L, "이름1", "01012345678", userPoint);
        //when
        int decreasePoint = 500;
        user.decreasePoint(decreasePoint);
        //then
        assertThat(user.getPoint()).isEqualTo(userPoint - decreasePoint);
    }
}