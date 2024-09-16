package com.zhy.utils;

import com.zhy.apicommon.common.ErrorCode;
import com.zhy.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedissonLockUtil {

    @Resource
    private RedissonClient redissonClient;

    public void redissonDistributedLocks(String lockName, Runnable runnable, String errorMessage) {
        redissonDistributedLocks(lockName, runnable, ErrorCode.OPERATION_ERROR, errorMessage);
    }

    /**
     * redisson分布式锁
     * @param lockName
     * @param runnable
     * @param errorCode
     * @param errorMessage
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, ErrorCode errorCode, String errorMessage) {

        RLock dlock = redissonClient.getLock(lockName);
        try {
            if (dlock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                runnable.run();
            }
            else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, errorMessage);
        } finally {
            if (dlock.isHeldByCurrentThread()) {
                log.info("lockName:{},unLockId:{} ", lockName, Thread.currentThread().getId());
                dlock.unlock();
            }
        }

    }


}
