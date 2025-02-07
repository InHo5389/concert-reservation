package concertreservation.token.service;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.service.redis.WaitingTokenRedisRepository;
import concertreservation.token.service.response.TokenStatusResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WaitingTokenRedisServiceMockTest {

    @InjectMocks
    private WaitingTokenRedisService waitingTokenRedisService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private WaitingTokenRedisRepository waitingTokenRedisRepository;

    @Test
    @DisplayName("토큰이 active 상태일때 TokenStatus ACTIVE를 반환하고 대기번호 0을 반환한다.")
    void shouldReturnActiveStatusWhenTokenIsActive() {
        //given
        String token = "token";
        Long userId = 1L;
        Long concertId = 1L;
        given(tokenProvider.getUserIdFromToken(token)).willReturn(userId);
        given(tokenProvider.getConcertIdFromToken(token)).willReturn(concertId);
        given(waitingTokenRedisRepository.isActive(userId,concertId)).willReturn(true);
        //when
        TokenStatusResponse response = waitingTokenRedisService.getTokenStatus(token);
        //then
        assertThat(response).extracting("tokenStatus", "waitingNumber")
                .containsExactlyInAnyOrder(TokenStatus.ACTIVE, 0L);
    }

    @Test
    @DisplayName("토큰이 waiting 상태일때 TokenStatus WAITING을 반환한다")
    void shouldReturnWaitingStatusWhenTokenIsWaiting() {
        //given
        String token = "token";
        Long userId = 1L;
        Long concertId = 1L;
        given(tokenProvider.getUserIdFromToken(token)).willReturn(userId);
        given(tokenProvider.getConcertIdFromToken(token)).willReturn(concertId);
        given(waitingTokenRedisRepository.isActive(userId,concertId)).willReturn(false);
        long rank = 4L;
        given(waitingTokenRedisRepository.getRank(userId,concertId)).willReturn(rank);
        //when
        TokenStatusResponse response = waitingTokenRedisService.getTokenStatus(token);
        //then
        assertThat(response).extracting("tokenStatus", "waitingNumber")
                .containsExactlyInAnyOrder(TokenStatus.WAITING, rank + 1);
    }

    @Test
    @DisplayName("토큰이 만료되었을때 TokenStatus EXPIRED를 반환한다")
    void shouldReturnExpiredStatusWhenTokenIsExpired(){
        //given
        String token = "token";
        Long userId = 1L;
        Long concertId = 1L;
        given(tokenProvider.getUserIdFromToken(token)).willReturn(userId);
        given(tokenProvider.getConcertIdFromToken(token)).willReturn(concertId);
        given(waitingTokenRedisRepository.isActive(userId,concertId)).willReturn(false);
        given(waitingTokenRedisRepository.getRank(userId,concertId)).willReturn(null);
        //when
        TokenStatusResponse response = waitingTokenRedisService.getTokenStatus(token);
        //then
        assertThat(response).extracting("tokenStatus", "waitingNumber")
                .containsExactlyInAnyOrder(TokenStatus.EXPIRED, null);
    }
}