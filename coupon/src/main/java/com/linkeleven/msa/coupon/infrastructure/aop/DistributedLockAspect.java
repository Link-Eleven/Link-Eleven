package com.linkeleven.msa.coupon.infrastructure.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class DistributedLockAspect {

	private final RedissonClient redissonClient;

	@Around("@annotation(DistributedLock)")
	public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
		String key = "LOCK:" + distributedLock.key();

		RLock rLock = redissonClient.getLock(key);

		try {
			boolean available = rLock.tryLock(distributedLock.waitTime(),
				distributedLock.leaseTime(),
				distributedLock.timeUnit());
			if (!available) {
				throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
			}
			return joinPoint.proceed();
		} finally {
			if (rLock.isHeldByCurrentThread()) {
				rLock.unlock();
			}
		}
	}
}
