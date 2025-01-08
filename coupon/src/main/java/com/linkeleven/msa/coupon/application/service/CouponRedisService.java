package com.linkeleven.msa.coupon.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponRedisService {

	private final RedisTemplate<String, String> redisTemplate;

	// 정책별로 쿠폰을 Redis에 추가
	public void addCouponsToRedis(Long couponId, Long policyId, int quantity) {
		String redisKey = "coupon:" + couponId + ":" + policyId;

		for (int i = 0; i < quantity; i++) {
			redisTemplate.opsForList().rightPush(redisKey, policyId + ":" + UUID.randomUUID());
		}
	}

	// 정책별로 Redis에서 쿠폰 발급 (우선순위에 따라)
	public String issueCouponFromRedis(Long couponId, List<Long> policyIds) {
		for (Long policyId : policyIds) {
			String redisKey = "coupon:" + couponId + ":" + policyId;
			String couponCode = redisTemplate.opsForList().leftPop(redisKey);

			if (couponCode != null) {
				return couponCode;  // 발급 성공
			}
		}
		return null;  // 모든 정책 쿠폰 소진
	}

	// 남아있는 쿠폰 개수 확인
	public Long getRemainingCoupons(Long couponId, Long policyId) {
		String redisKey = "coupon:" + couponId + ":" + policyId;
		return redisTemplate.opsForList().size(redisKey);
	}
}
