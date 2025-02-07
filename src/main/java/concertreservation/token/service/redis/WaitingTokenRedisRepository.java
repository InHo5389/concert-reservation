package concertreservation.token.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String ACTIVE_KEY = "active_queue:concert:%s";
    private static final String WAITING_KEY = "waiting_queue:concert:%s";

    public boolean issue(Long userId, Long concertId, double score) {
        return redisTemplate.opsForZSet().add(generateWaitingKey(concertId), String.valueOf(userId), score);
    }

    public boolean isActive(Long userId, Long concertId) {
        Double score = redisTemplate.opsForZSet().score(
                generateActiveKey(concertId),
                String.valueOf(userId)
        );
        return score != null;
    }

    public Long getRank(Long userId, Long concertId) {
        return redisTemplate.opsForZSet().rank(generateWaitingKey(concertId), String.valueOf(userId));
    }

    public boolean existsWaitingToken(Long concertId, String member) {
        Double score = redisTemplate.opsForZSet().score(generateWaitingKey(concertId), member);
        return score != null;
    }

    public boolean existsActiveToken(Long concertId, String member) {
        Double score = redisTemplate.opsForZSet().score(generateActiveKey(concertId), member);
        return score != null;
    }

    public void removeAllExpiredTokens(Long concertId, long currentTimeMills) {
        redisTemplate.opsForZSet().removeRangeByScore(
                generateActiveKey(concertId),
                Double.NEGATIVE_INFINITY,
                currentTimeMills
        );
    }

    public void moveTopWaitingToActive(int limit, Long concertId, int expiredMinute) {
        // score 없이 value만 조회
        Set<String> members = redisTemplate.opsForZSet()
                .range(generateWaitingKey(concertId), 0, limit - 1);

        if (members != null && !members.isEmpty()) {
            redisTemplate.execute(new SessionCallback<List<Object>>() {
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();

                    // active queue에 추가
                    members.forEach(member -> {
                        long memberExpiredScore = System.currentTimeMillis() +
                                TimeUnit.MINUTES.toMillis(expiredMinute);
                        operations.opsForZSet()
                                .add(generateActiveKey(concertId), member, memberExpiredScore);
                    });

                    // waiting queue에서 삭제
                    operations.opsForZSet().remove(
                            generateWaitingKey(concertId),
                            members.toArray());

                    return operations.exec();
                }
            });
        }
    }

    public Set<String> getActiveWaitingConcertIds(String queueType) {
        // concert:*:waiting_queue 패턴으로 key 검색
        String pattern = String.format("%s:concert:*", queueType);
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys == null || keys.isEmpty()) {
            return Collections.emptySet();
        }

        // key에서 concert ID만 추출
        return keys.stream()
                .map(key -> key.split(":")[2]) // waiting_queue:concert:1 -> 1
                .collect(Collectors.toSet());
    }

    public void issueActiveQueue(Long userId,Long concertId,long currentTimeMillis){
        redisTemplate.opsForZSet().add(generateActiveKey(concertId),String.valueOf(userId),currentTimeMillis);
    }

    public Long getActiveTokenCount(Long concertId){
        return redisTemplate.opsForZSet().zCard(generateActiveKey(concertId));
    }

    private String generateActiveKey(Long concertId) {
        return ACTIVE_KEY.formatted(String.valueOf(concertId));
    }

    private String generateWaitingKey(Long concertId) {
        return WAITING_KEY.formatted(String.valueOf(concertId));
    }
}
