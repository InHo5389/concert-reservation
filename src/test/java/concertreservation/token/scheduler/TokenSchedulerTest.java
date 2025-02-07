package concertreservation.token.scheduler;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.entity.WaitingToken;
import concertreservation.token.service.redis.WaitingTokenRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class TokenSchedulerTest {

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
    private TokenScheduler tokenScheduler;

    @Autowired
    private WaitingTokenRedisRepository waitingTokenRedisRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    @DisplayName("Redis에 만료시간 지난 Active Token을 4개 저장을 했을때 만료시간 지난 Active token을 없애는" +
            "스케줄러를 실행하면 Active Token을 조회할때 한개도 남아있지 않아야 한다.")
    void updateExpiredTokenStatus() {
        // given
        Long userId = 1L;
        Long concertId = 1L;
        long expiredTimeMillis = System.currentTimeMillis() - 100;
        waitingTokenRedisRepository.issueActiveQueue(userId,concertId,expiredTimeMillis);
        waitingTokenRedisRepository.issueActiveQueue(userId,concertId,expiredTimeMillis);
        waitingTokenRedisRepository.issueActiveQueue(userId,concertId,expiredTimeMillis);
        waitingTokenRedisRepository.issueActiveQueue(userId,concertId,expiredTimeMillis);
        // when
        tokenScheduler.removeExpiredTokens();
        // then
        boolean result = waitingTokenRedisRepository.existsActiveToken(concertId, String.valueOf(userId));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Active 토큰 2개 Wait 토큰 4개를 저장하고 1분마다 Wait 토큰에서 Active토큰으로 넘기는" +
            "스케줄러를 실행할때 Active토큰에 6개가 존재하여야 한다.")
    void activateFromWaiting() {
        // given
        Long concertId = 1L;
        long currentTimeMillis = System.currentTimeMillis();
        waitingTokenRedisRepository.issueActiveQueue(1L,concertId,currentTimeMillis);
        waitingTokenRedisRepository.issueActiveQueue(2L,concertId,currentTimeMillis);

        waitingTokenRedisRepository.issue(3L,concertId,currentTimeMillis);
        waitingTokenRedisRepository.issue(4L,concertId,currentTimeMillis);
        waitingTokenRedisRepository.issue(5L,concertId,currentTimeMillis);
        waitingTokenRedisRepository.issue(6L,concertId,currentTimeMillis);

        // when
        tokenScheduler.activateFromWaiting();

        // then
        Long activeTokenCount = waitingTokenRedisRepository.getActiveTokenCount(concertId);
        assertThat(activeTokenCount).isEqualTo(6);
    }
}