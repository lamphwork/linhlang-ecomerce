package linhlang.commons.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private Boolean enabled;
    private String host;
    private String publicUrl;
    private String user;
    private String pass;
}
