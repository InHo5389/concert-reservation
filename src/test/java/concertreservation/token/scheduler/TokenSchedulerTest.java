package concertreservation.token.scheduler;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.entity.WaitingToken;
import concertreservation.token.service.WaitingTokenRepository;
import concertreservation.token.util.TokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TokenSchedulerTest {

    @Autowired
    private TokenScheduler tokenScheduler;

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;

    @Test
    @DisplayName("스케줄러를 실행하면 만료된 토큰의 상태가 EXPIRED로 변경되어야 한다")
    void updateExpiredTokenStatus() {
        // given
        WaitingToken token = WaitingToken.create(1L, "token", 0);
        token.updateStatus(TokenStatus.ACTIVE, 0);
        waitingTokenRepository.save(token);

        // when
        tokenScheduler.updateExpiredTokenStatus(); // 즉시 실행됨

        // then
        WaitingToken updatedToken = waitingTokenRepository.findById(token.getId()).orElseThrow();
        assertThat(updatedToken.getStatus()).isEqualTo(TokenStatus.EXPIRED);
    }

    @Test
    @DisplayName("ACTIVE 토큰 MAX 값을 5라고 가정할 때 ACTIVE 토큰 2개 생성하고 " +
            "WAITING 토큰 4개 생성할때 스케줄러를 실행하면 ACTIVE 토큰은 5개 WAITING 토큰은 1개여야 한다")
    void activateFromWaiting() {
        // given
        WaitingToken activeToken1 = WaitingToken.create(1L, "token1", TokenUtil.EXPIRE_MINUTE);
        activeToken1.updateStatus(TokenStatus.ACTIVE, TokenUtil.EXPIRE_MINUTE);
        WaitingToken activeToken2 = WaitingToken.create(2L, "token2", TokenUtil.EXPIRE_MINUTE);
        activeToken2.updateStatus(TokenStatus.ACTIVE, TokenUtil.EXPIRE_MINUTE);

        WaitingToken waitingToken1 = WaitingToken.create(3L, "token3", TokenUtil.EXPIRE_MINUTE);
        WaitingToken waitingToken2 = WaitingToken.create(4L, "token4", TokenUtil.EXPIRE_MINUTE);
        WaitingToken waitingToken3 = WaitingToken.create(5L, "token5", TokenUtil.EXPIRE_MINUTE);
        WaitingToken waitingToken4 = WaitingToken.create(6L, "token6", TokenUtil.EXPIRE_MINUTE);

        waitingTokenRepository.saveAll(Arrays.asList(
                activeToken1, activeToken2,
                waitingToken1, waitingToken2, waitingToken3, waitingToken4
        ));

        // when
        tokenScheduler.activateFromWaiting();

        // then
        List<WaitingToken> activeTokens = waitingTokenRepository.findByStatus(TokenStatus.ACTIVE);
        assertThat(activeTokens).hasSize(5);

        List<WaitingToken> waitingTokens = waitingTokenRepository.findByStatus(TokenStatus.WAITING);
        assertThat(waitingTokens.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("EXPIRED 상태의 토큰들이 삭제되어야 한다")
    void removeExpiredTokens() {
        // given
        // ACTIVE, WAITING 토큰 생성
        WaitingToken activeToken = WaitingToken.create(1L, "token1", TokenUtil.EXPIRE_MINUTE);
        activeToken.updateStatus(TokenStatus.ACTIVE, TokenUtil.EXPIRE_MINUTE);
        WaitingToken waitingToken = WaitingToken.create(2L, "token2", TokenUtil.EXPIRE_MINUTE);

        // EXPIRED 토큰 생성
        WaitingToken expiredToken1 = WaitingToken.create(3L, "token3", 0);
        expiredToken1.updateStatus(TokenStatus.EXPIRED, 0);
        WaitingToken expiredToken2 = WaitingToken.create(4L, "token4", 0);
        expiredToken2.updateStatus(TokenStatus.EXPIRED, 0);

        waitingTokenRepository.saveAll(Arrays.asList(
                activeToken, waitingToken, expiredToken1, expiredToken2
        ));

        // when
        tokenScheduler.removeExpiredTokens();

        // then
        List<WaitingToken> remainingTokens = waitingTokenRepository.findAll();
        assertThat(remainingTokens)
                .hasSize(2)
                .extracting("status")
                .containsOnly(TokenStatus.ACTIVE, TokenStatus.WAITING);
    }

    @TestConfiguration
    static class TestTokenConfig {
        @Bean
        public TokenUtil tokenUtil() {
            return new TokenUtil() {
                public int getMaxActiveTokens() {
                    return 5;
                }

                public int getExpireMinute() {
                    return 5;
                }
            };
        }
    }
}