package com.linkeleven.msa.coupon.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
	public Optional<String> issueCouponFromRedis(Long couponId, List<Long> policyIds) {
		for (Long policyId : policyIds) {
			String redisKey = "coupon:" + couponId + ":" + policyId;
			String couponCode = redisTemplate.opsForList().leftPop(redisKey);

			if (couponCode != null) {
				return Optional.of(couponCode);  // 발급 성공
			}
		}
		return Optional.empty();  // 모든 정책 쿠폰 소진
	}

	// 중복 발급 체크 메소드 추가
	public boolean checkAndSetUserCoupon(Long userId, Long couponId) {
		String userCouponKey = "user_coupon:" + userId + ":" + couponId;
		return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(userCouponKey, "1", 24, TimeUnit.HOURS));
	}

	// 중복 발급 체크 키 삭제 메소드 추가
	public void deleteUserCouponCheck(Long userId, Long couponId) {
		String userCouponKey = "user_coupon:" + userId + ":" + couponId;
		redisTemplate.delete(userCouponKey);
	}
}
