package linhlang.commons.persistence;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.logging.LogManager;

@Slf4j
@Configuration
@ComponentScan("linhlang.commons.persistence")
public class PersistenceConfig {

    static {
        LogManager.getLogManager().reset();
    }

    @Bean
    DSLContext dslContext(final DataSource dataSource) {
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
        defaultConfiguration.setDataSource(dataSource);
        defaultConfiguration.setSQLDialect(SQLDialect.MYSQL);
        defaultConfiguration.set(new DefaultExecuteListenerProvider(new CustomJooqLogger()));
        return DSL.using(defaultConfiguration);
    }
}
