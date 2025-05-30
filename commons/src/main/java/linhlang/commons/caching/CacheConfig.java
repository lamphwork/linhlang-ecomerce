package linhlang.commons.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Configuration
@EnableCaching
@Import({CacheProperties.class})
@ConditionalOnProperty(value = "redis.enabled", havingValue = "true")
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(final RedisConnectionFactory connectionFactory, final CacheProperties cacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new CustomValueSerializer<>(Object.class)));

        Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();
        cacheProperties.getEntries().forEach(cacheEntry -> {
            List<String> cacheNames = Stream.of(cacheEntry.getCacheNames().split(","))
                    .map(String::trim)
                    .filter(str -> !str.isBlank())
                    .toList();
            for (String cacheName : cacheNames) {
                log.info("Setting cache {} with ttl: {}", cacheName, cacheEntry.getTtl());
                cacheConfig.put(
                        cacheName,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(cacheEntry.getTtl()))
                );
            }
        });

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfig)
                .build();
    }

}
