package com.linkeleven.msa.coupon.application.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponRedisService {

	private final RedisTemplate<String, String> redisTemplate;

	public void saveCouponsToRedis(Long couponId, Long policyId, int quantity, int discountRate) {
		String redisKey = "coupon:" + couponId + ":" + policyId;

		for (int i = 0; i < quantity; i++) {
			redisTemplate.opsForList().rightPush(redisKey, policyId + ":" + discountRate + "%");
		}
	}

	public Optional<String> issueCouponFromRedis(Long couponId, List<Long> policyIds) {
		for (Long policyId : policyIds) {
			String redisKey = "coupon:" + couponId + ":" + policyId;
			String couponCode = redisTemplate.opsForList().leftPop(redisKey);

			if (couponCode != null) {
				log.info("couponCode:" + couponCode + "쿠폰 코드 발급 성공");
				return Optional.of(couponCode);
			}
		}
		return Optional.empty();
	}

	public void savePolicies(Long couponId, List<CouponPolicy> policies) {
		policies.forEach(policy -> saveCouponsToRedis(couponId, policy.getPolicyId(),
			policy.getQuantity(), policy.getDiscountRate()));
	}

	public boolean isUserCouponAlreadyIssued(Long userId, Long couponId) {
		String userCouponKey = "user_coupon:" + userId + ":" + couponId;
		return Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(userCouponKey, "1", 1, TimeUnit.HOURS));
	}

	public void deleteUserCouponCheck(Long userId, Long couponId) {
		String userCouponKey = "user_coupon:" + userId + ":" + couponId;
		redisTemplate.delete(userCouponKey);
	}
}
