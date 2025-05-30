package linhlang.commons.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Aspect
@Configuration
public class LoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethod() {}

    @Before("serviceMethod()")
    public void logBeforeServiceMethods(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof  MethodSignature methodSignature) {
            Object[] args = joinPoint.getArgs();
            log.info("Call method {} with params: {}", methodSignature.getMethod().getName(), args);
        }
    }
}
