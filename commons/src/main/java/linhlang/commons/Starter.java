package linhlang.commons;

import linhlang.commons.security.SecurityConfig;
import linhlang.commons.web.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class Starter {

    public static SpringApplication webApp(Class<?> mainApp) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(mainApp);
        builder.sources(SecurityConfig.class);
        builder.sources(WebConfiguration.class);
        return builder.build();
    }
}
