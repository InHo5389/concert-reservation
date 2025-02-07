package concertreservation.token.service;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.service.redis.WaitingTokenRedisRepository;
import concertreservation.token.service.response.TokenIssueResponse;
import concertreservation.token.service.response.TokenStatusResponse;
import concertreservation.token.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WaitingTokenRedisService {

    private final TokenProvider tokenProvider;
    private final WaitingTokenRedisRepository waitingTokenRedisRepository;

    @Transactional
    public TokenIssueResponse issueToken(Long userId, Long concertId) {
        String jwtToken = tokenProvider.createToken(userId, concertId);
        long score = System.currentTimeMillis();
        waitingTokenRedisRepository.issue(userId, concertId, score);

        return TokenIssueResponse.from(jwtToken, TokenStatus.WAITING);
    }

    @Transactional(readOnly = true)
    public TokenStatusResponse getTokenStatus(String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);
        Long concertId = tokenProvider.getConcertIdFromToken(token);

        if (waitingTokenRedisRepository.isActive(userId, concertId)) {
            return new TokenStatusResponse(token, TokenStatus.ACTIVE, 0L, LocalDateTime.now().plusMinutes(TokenUtil.EXPIRE_MINUTE));
        }

        Long rank = waitingTokenRedisRepository.getRank(userId, concertId);
        if (rank != null) {
            return new TokenStatusResponse(token, TokenStatus.WAITING, rank + 1, LocalDateTime.now().plusMinutes(TokenUtil.EXPIRE_MINUTE));
        }

        return new TokenStatusResponse(token, TokenStatus.EXPIRED, null, null);
    }

    @Transactional
    public void removeExpiredTokens(Long concertId) {
        waitingTokenRedisRepository.removeAllExpiredTokens(concertId, System.currentTimeMillis());
    }

    @Transactional
    public void updateActivateFromWaiting(Long concertId) {
        waitingTokenRedisRepository.moveTopWaitingToActive(TokenUtil.MAX_ACTIVE_TOKENS, concertId, TokenUtil.EXPIRE_MINUTE);
    }

    public Set<String> getActiveWaitingConcertIds(String queueType) {
        return waitingTokenRedisRepository.getActiveWaitingConcertIds(queueType);
    }
}
