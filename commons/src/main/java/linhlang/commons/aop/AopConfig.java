package linhlang.commons.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({LoggingAspect.class, LockBusinessAspect.class})
public class AopConfig {
}
