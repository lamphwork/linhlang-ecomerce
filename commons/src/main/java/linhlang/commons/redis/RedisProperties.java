package linhlang.commons.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private boolean enabled;
    private String nodes;
    private String username;
    private String password;
}
