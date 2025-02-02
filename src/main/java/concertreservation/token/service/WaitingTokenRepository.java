package concertreservation.token.service;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.entity.WaitingToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingTokenRepository {

    Optional<WaitingToken> findById(Long tokenId);
    WaitingToken save(WaitingToken waitingToken);
    Optional<WaitingToken> findByToken(String token);
    List<WaitingToken> findByStatusOrderByIdAsc(TokenStatus status);
    int countByStatus(TokenStatus status);
    List<WaitingToken> findByStatusInAndExpiredAtBefore(List<TokenStatus> statuses, LocalDateTime now);
    long countByStatusAndIdLessThan(TokenStatus status,Long id);
    Optional<WaitingToken> findByUserIdAndStatusIn(Long userId, List<TokenStatus> statuses);
    void deleteByStatus(TokenStatus tokenStatus);

    List<WaitingToken> findByStatus(TokenStatus tokenStatus);
    List<WaitingToken> saveAll(List<WaitingToken> waitingTokens);
    List<WaitingToken> findAll();

}
