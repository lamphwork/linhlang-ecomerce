package linhlang.commons.aop;

import jakarta.annotation.Resource;
import linhlang.commons.aop.annotation.LockBusiness;
import linhlang.commons.redis.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

@Aspect
@Configuration
@ConditionalOnProperty(value = "redis.enabled", havingValue = "true")
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class LockBusinessAspect {

    @Lazy
    @Resource
    private RedisLock redisLock;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Pointcut("@annotation(linhlang.commons.aop.annotation.LockBusiness)")
    public void lockedMethod() {
    }

    @Around("lockedMethod()")
    public Object safeExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature methodSignature) {
            Method method = methodSignature.getMethod();
            LockBusiness lockBusiness = method.getAnnotation(LockBusiness.class);

            String businessKey = lockBusiness.business();
            String key = resolveKey(lockBusiness.key(), joinPoint);
            long timeOut = lockBusiness.timeout();

            String lockKey = businessKey + "_" + key;
            try {
                redisLock.waitLock(lockKey, timeOut);
                return joinPoint.proceed();
            } finally {
                redisLock.releaseLock(lockKey);
            }
        }

        throw new RuntimeException("can not proxy method");
    }

    private String resolveKey(String keyExpression, ProceedingJoinPoint joinPoint) {
        if (keyExpression == null || keyExpression.isBlank()) {
            return "EMPTY";
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = methodSignature.getParameterNames();

        // Set up SpEL context
        EvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        // Parse the expression
        String result = expressionParser.parseExpression(keyExpression).getValue(context, String.class);
        if (result == null) {
            throw new RuntimeException("can not resolve key expression: " + keyExpression);
        }
        return result;
    }
}
