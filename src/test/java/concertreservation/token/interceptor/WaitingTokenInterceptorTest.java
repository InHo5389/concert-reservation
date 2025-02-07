package concertreservation.token.interceptor;

import concertreservation.token.service.WaitingTokenRedisService;
import concertreservation.token.service.response.TokenIssueResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WaitingTokenInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WaitingTokenRedisService waitingTokenRedisService;

    @Test
    @DisplayName("유효한 토큰으로 요청 시 정상 처리되어야 한다")
    void validTokenAccess() throws Exception {
        // given
        TokenIssueResponse tokenResponse = waitingTokenRedisService.issueToken(1L, 1L);

        // when & then
        mockMvc.perform(get("/waiting-token/status")
                        .header("WaitingToken", tokenResponse.getToken()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 요청 시 401 에러가 반환되어야 한다")
    void invalidTokenAccess() throws Exception {
        mockMvc.perform(get("/waiting-token/status")
                        .header("WaitingToken", "invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("토큰 없이 요청 시 401 에러가 반환되어야 한다")
    void noTokenAccess() throws Exception {
        mockMvc.perform(get("/waiting-token/status"))
                .andExpect(status().isUnauthorized());
    }
}