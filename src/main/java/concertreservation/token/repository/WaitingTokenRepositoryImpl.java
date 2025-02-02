package concertreservation.token.repository;

import concertreservation.token.entity.TokenStatus;
import concertreservation.token.entity.WaitingToken;
import concertreservation.token.service.WaitingTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRepositoryImpl implements WaitingTokenRepository {

    private final WaitingTokenjJpaRepository waitingTokenjJpaRepository;

    @Override
    public Optional<WaitingToken> findById(Long tokenId) {
        return waitingTokenjJpaRepository.findById(tokenId);
    }

    @Override
    public WaitingToken save(WaitingToken waitingToken) {
        return waitingTokenjJpaRepository.save(waitingToken);
    }

    @Override
    public Optional<WaitingToken> findByToken(String token) {
        return waitingTokenjJpaRepository.findByToken(token);
    }

    @Override
    public List<WaitingToken> findByStatusOrderByIdAsc(TokenStatus status) {
        return waitingTokenjJpaRepository.findByStatusOrderByIdAsc(status);
    }

    @Override
    public int countByStatus(TokenStatus status) {
        return waitingTokenjJpaRepository.countByStatus(status);
    }

    @Override
    public List<WaitingToken> findByStatusInAndExpiredAtBefore(List<TokenStatus> statuses, LocalDateTime now) {
        return waitingTokenjJpaRepository.findByStatusInAndExpiredAtBefore(statuses, now);
    }

    @Override
    public long countByStatusAndIdLessThan(TokenStatus status, Long id) {
        return waitingTokenjJpaRepository.countByStatusAndIdLessThan(status, id);
    }

    @Override
    public Optional<WaitingToken> findByUserIdAndStatusIn(Long userId, List<TokenStatus> statuses) {
        return waitingTokenjJpaRepository.findByUserIdAndStatusIn(userId, statuses);
    }

    @Override
    public void deleteByStatus(TokenStatus tokenStatus) {
        waitingTokenjJpaRepository.deleteByStatus(tokenStatus);
    }

    @Override
    public List<WaitingToken> findByStatus(TokenStatus tokenStatus) {
        return waitingTokenjJpaRepository.findByStatus(tokenStatus);
    }

    @Override
    public List<WaitingToken> saveAll(List<WaitingToken> waitingTokens) {
        return waitingTokenjJpaRepository.saveAll(waitingTokens);
    }

    @Override
    public List<WaitingToken> findAll() {
        return waitingTokenjJpaRepository.findAll();
    }
}
