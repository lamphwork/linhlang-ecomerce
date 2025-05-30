package linhlang.commons.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "web.enable-swagger", havingValue = "true")
public class SwaggerConfig {

}
