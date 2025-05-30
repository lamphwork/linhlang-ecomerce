package linhlang.commons.caching;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private String type;
    private List<CacheEntry> entries;

    @Data
    public static class CacheEntry {

        private String cacheNames;
        private Long ttl;
    }
}
