package linhlang.commons;

import linhlang.commons.aop.AopConfig;
import linhlang.commons.caching.CacheConfig;
import linhlang.commons.persistence.PersistenceConfig;
import linhlang.commons.redis.RedisConfig;
import linhlang.commons.security.SecurityConfig;
import linhlang.commons.storage.StorageConfig;
import linhlang.commons.web.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class Starter {

    public static SpringApplication webApp(Class<?> mainApp) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(mainApp);
        builder.web(WebApplicationType.SERVLET);
        builder.sources(StorageConfig.class);
        builder.sources(SecurityConfig.class);
        builder.sources(WebConfiguration.class);
        builder.sources(AopConfig.class);
        builder.sources(RedisConfig.class);
        builder.sources(CacheConfig.class);
        builder.sources(StorageConfig.class);
        builder.sources(PersistenceConfig.class);
        return builder.build();
    }
}
