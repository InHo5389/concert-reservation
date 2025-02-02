package concertreservation.token.service;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.entity.WaitingToken;
import concertreservation.token.service.response.TokenIssueResponse;
import concertreservation.token.service.response.TokenStatusResponse;
import concertreservation.token.util.TokenUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingTokenServiceTest {

    @InjectMocks
    private WaitingTokenService waitingTokenService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private WaitingTokenRepository waitingTokenRepository;

    @Test
    @DisplayName("토큰 발급 시 새로운 대기 토큰이 생성되어야 하고 WAITING상태여야 한다.")
    void issueToken() {
        //given
        Long userId = 1L;
        String jwtToken = "test-token";
        WaitingToken waitingToken = WaitingToken.create(userId, jwtToken, TokenUtil.EXPIRE_MINUTE);

        given(tokenProvider.createToken(userId)).willReturn(jwtToken);
        given(waitingTokenRepository.save(any(WaitingToken.class))).willReturn(waitingToken);
        //when
        TokenIssueResponse response = waitingTokenService.issueToken(userId);
        //then
        assertThat(response.getToken()).isEqualTo(jwtToken);
        assertThat(response.getTokenStatus()).isEqualTo(TokenStatus.WAITING);
        verify(waitingTokenRepository).findByUserIdAndStatusIn(eq(userId), any());
    }

    @Test
    @DisplayName("대기 상태의 토큰 조회 시 대기 순번이 반환되어야 한다")
    void getWaitingTokenStatus() {
        //given
        String token = "test-token";

        WaitingToken waitingToken = WaitingToken.create(1L, token, TokenUtil.EXPIRE_MINUTE);
        given(waitingTokenRepository.findByToken(token)).willReturn(Optional.of(waitingToken));
        given(waitingTokenRepository.countByStatusAndIdLessThan(TokenStatus.WAITING, waitingToken.getId()))
                .willReturn(5L);
        //when
        TokenStatusResponse response = waitingTokenService.getTokenStatus(token);
        //then
        assertThat(response).extracting("token", "tokenStatus", "waitingNumber")
                .containsExactlyInAnyOrder(token, TokenStatus.WAITING, 5L + 1);
    }

    @Test
    @DisplayName("대기 상태의 만료 토큰 조회 시 대기 순번은 null이 반환되어야 한다.")
    void getExpiredWaitingTokenStatus(){
        //given
        String token = "test-token";

        WaitingToken waitingToken = WaitingToken.builder().userId(1L).token(token).status(TokenStatus.EXPIRED).build();
        given(waitingTokenRepository.findByToken(token)).willReturn(Optional.of(waitingToken));
        //when
        TokenStatusResponse response = waitingTokenService.getTokenStatus(token);
        //then
        assertThat(response).extracting("token", "tokenStatus", "waitingNumber")
                .containsExactlyInAnyOrder(token, TokenStatus.EXPIRED, null);
    }

    @Test
    @DisplayName("대기 상태의 활성 토큰 조회 시 대기 순번은 0이 반환되어야 한다.")
    void getActiveWaitingTokenStatus(){
        //given
        String token = "test-token";

        WaitingToken waitingToken = WaitingToken.builder().userId(1L).token(token).status(TokenStatus.ACTIVE).build();
        given(waitingTokenRepository.findByToken(token)).willReturn(Optional.of(waitingToken));
        //when
        TokenStatusResponse response = waitingTokenService.getTokenStatus(token);
        //then
        assertThat(response).extracting("token", "tokenStatus", "waitingNumber")
                .containsExactlyInAnyOrder(token, TokenStatus.ACTIVE, 0L);
    }
}