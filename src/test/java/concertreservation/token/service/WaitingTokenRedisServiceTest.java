package concertreservation.token.service;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.service.redis.WaitingTokenRedisRepository;
import concertreservation.token.service.response.TokenIssueResponse;
import concertreservation.token.service.response.TokenStatusResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
class WaitingTokenRedisServiceTest {

    @Container
    private static final GenericContainer redis = new GenericContainer("redis:7.0.8-alpine")
            .withExposedPorts(6379)
            .withReuse(true);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379).toString());
    }

    @Autowired
    private WaitingTokenRedisService waitingTokenRedisService;

    @Autowired
    private WaitingTokenRedisRepository waitingTokenRedisRepository;

    @Test
    @DisplayName("토큰을 발급하면 redis에 waiting_queue키로 저장되고 redis repository에서 그걸 조회했을때 true가 나와야 한다.")
    void issueToken() {
        //given
        Long userId = 1L;
        Long concertId = 1L;
        //when
        TokenIssueResponse response = waitingTokenRedisService.issueToken(userId, concertId);
        //then
        boolean result = waitingTokenRedisRepository.existsWaitingToken(concertId, String.valueOf(userId));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Redis에 waiting_queue에 토큰이 있을 경우 대기번호 4개 있을경우 마지막 토큰으로 대기번호를 조회하면" +
            "대기번호 4가 출력되어야 한다.")
    void getTokenStatus() {
        //given
        Long concertId = 1L;
        waitingTokenRedisService.issueToken(1L, concertId);
        waitingTokenRedisService.issueToken(2L, concertId);
        waitingTokenRedisService.issueToken(3L, concertId);
        long userId = 4L;
        TokenIssueResponse issueResponse = waitingTokenRedisService.issueToken(userId, concertId);
        //when
        TokenStatusResponse statusResponse = waitingTokenRedisService.getTokenStatus(issueResponse.getToken());
        //then
        assertThat(statusResponse).extracting("tokenStatus", "waitingNumber")
                .containsExactlyInAnyOrder(TokenStatus.WAITING, userId);
    }
}