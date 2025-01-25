package concertreservation.concert.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorType;
import concertreservation.concert.entity.Concert;
import concertreservation.concert.service.ConcertRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConcertRedisRepositoryImpl implements ConcertRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String KEY_FORMAT = "concert::list::%d::%d";

    public List<Concert> readAllInfiniteScroll(Long lastConcertId, Long limit) {
        Set<String> stringConcerts = redisTemplate.opsForZSet().reverseRange(
                generateKey(limit, lastConcertId),
                0,
                limit - 1
        );

        return stringConcerts.stream()
                .map(concertStr -> {
                    try {
                        return objectMapper.readValue(concertStr, Concert.class);
                    } catch (JsonProcessingException e) {
                        throw new CustomGlobalException(ErrorType.JSON_PARSING_FAILED);
                    }
                })
                .collect(Collectors.toList());
    }

    public void add(List<Concert> concerts, Long lastConcertId, Long limit) {
        redisTemplate.executePipelined((RedisCallback<?>) connection -> {
            StringRedisConnection conn = (StringRedisConnection) connection;
            for (Concert concert : concerts) {
                try {
                    String value = objectMapper.writeValueAsString(concert);
                    conn.zAdd(generateKey(limit, lastConcertId), concert.getId(), value);
                } catch (JsonProcessingException e) {
                    throw new CustomGlobalException(ErrorType.JSON_PARSING_FAILED);
                }
            }
            return null;
        });
    }

    private String generateKey(Long limit, Long lastConcertId) {
        return KEY_FORMAT.formatted(limit, lastConcertId);
    }
}
