package linhlang.commons.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLock {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Map<String, Object> lockMap = new ConcurrentHashMap<>();

    /**
     * redis lock
     *
     * @param key redis key
     * @param ttl time to live in seconds
     */
    public boolean tryLock(String key, long ttl) {
        boolean success = Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(key, (short) 1, ttl, TimeUnit.SECONDS)
        );
        if (success) {
            log.info("Locked business by key: {}", key);
            lockMap.put(key, new Object());
        }
        return success;
    }

    public void releaseLock(String key) {
        redisTemplate.opsForValue().getAndDelete(key);
        log.info("release log for business {}", key);
        Object lockedObj = lockMap.get(key);
        if (lockedObj != null) {
            synchronized (lockedObj) {
                lockedObj.notify();
            }
            lockMap.remove(key);
        }
    }

    public void waitLock(String key, long ttl) {
        long waitTime = ttl * 1000; // convert seconds to milliseconds
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < waitTime) {
            boolean lock = tryLock(key, waitTime);
            if (lock) {
                return;
            }
            try {
                Object lockedObj = lockMap.get(key);
                if (lockedObj == null) {
                    continue;
                }

                synchronized (lockedObj) {
                    log.info("Business locked by key {}, waiting", key);
                    lockedObj.wait(waitTime);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
