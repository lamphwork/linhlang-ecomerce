package linhlang.commons;

import linhlang.commons.aop.AopConfig;
import linhlang.commons.caching.CacheConfig;
import linhlang.commons.persistence.PersistenceConfig;
import linhlang.commons.redis.RedisConfig;
import linhlang.commons.security.SecurityConfig;
import linhlang.commons.storage.StorageConfig;
import linhlang.commons.web.WebConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
@Import({
        AopConfig.class,
        RedisConfig.class,
        CacheConfig.class,
        WebConfiguration.class,
        SecurityConfig.class,
        StorageConfig.class,
        PersistenceConfig.class,
})
public @interface CommonWebApp {
}
