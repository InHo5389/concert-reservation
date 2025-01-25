package concertreservation.common.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    GenericJackson2JsonRedisSerializer jsonRedisSerializer() {
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        jsonRedisSerializer.configure(objectMapper -> {
            objectMapper.registerModule(new JavaTimeModule());
        });

        return jsonRedisSerializer;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, GenericJackson2JsonRedisSerializer serializer) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(SerializationPair.fromSerializer(serializer)))
                .build();
    }

}
