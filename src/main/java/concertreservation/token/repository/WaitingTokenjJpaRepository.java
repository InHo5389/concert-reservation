package concertreservation.token.repository;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.entity.WaitingToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingTokenjJpaRepository extends JpaRepository<WaitingToken,Long> {
    Optional<WaitingToken> findByToken(String token);
    List<WaitingToken> findByStatusOrderByIdAsc(TokenStatus status);
    int countByStatus(TokenStatus status);
    List<WaitingToken> findByStatusInAndExpiredAtBefore(List<TokenStatus> statuses, LocalDateTime now);
    long countByStatusAndIdLessThan(TokenStatus status,Long id);
    Optional<WaitingToken> findByUserIdAndStatusIn(Long userId, List<TokenStatus> statuses);
    void deleteByStatus(TokenStatus tokenStatus);
    List<WaitingToken> findByStatus(TokenStatus tokenStatus);
}
