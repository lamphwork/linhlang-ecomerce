package linhlang.commons.storage;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = "linhlang.commons.storage")
public class StorageConfig {

    @Bean
    @ConditionalOnProperty(value = "storage.enabled", havingValue = "true")
    public MinioClient minioClient(final StorageProperties config) {
        log.info("Using storage: {}", config.getHost());
        return MinioClient.builder()
                .endpoint(config.getHost())
                .credentials(config.getUser(), config.getPass())
                .build();
    }
}
