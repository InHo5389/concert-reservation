package concertreservation.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import concertreservation.user.controller.request.PointChargeRequest;
import concertreservation.user.service.UserService;
import concertreservation.user.service.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("포인트를 조회한다.")
    void readPoint() throws Exception {
        //given
        Long userId = 1L;
        User user = new User(1L, "홍길동", "01012345678", 1000);
        //when
        //then
        mockMvc.perform(
                get("/users/{userId}/point",userId)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포인트를 충전한다.")
    void chargePoint() throws Exception {
        //given
        Long userId = 1L;
        int point = 500;
        PointChargeRequest request = new PointChargeRequest(userId, point);
        //when
        //then
        mockMvc.perform(
                patch("/users/point",userId)
                        .content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}