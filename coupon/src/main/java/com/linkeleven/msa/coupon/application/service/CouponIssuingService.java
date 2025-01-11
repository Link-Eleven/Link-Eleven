package com.linkeleven.msa.coupon.application.service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.infrastructure.aop.DistributedLock;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponIssuingService {
	private final CouponPolicyRepository couponPolicyRepository;
	private final IssuedCouponRepository issuedCouponRepository;
	private final CouponRedisService couponRedisService;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	public IssuedCouponDto issueCoupon(Long userId, String role, Long couponId) {
		// // 현재 시간이 자정 00시 이전인 경우 쿠폰 발급을 막음
		// LocalDateTime currentTime = LocalDateTime.now();
		// LocalDateTime midnight = currentTime.toLocalDate().atStartOfDay().plusDays(1); // 자정 00시
		//
		// if (currentTime.isBefore(midnight)) {
		// 	throw new CustomException(ErrorCode.COUPON_CANNOT_BE_ISSUED_YET);
		// }

		// 중복 발급 체크를 Redis로 이동
		String userCouponKey = "user_coupon:" + userId + ":" + couponId;
		if (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(userCouponKey, "1", 24, TimeUnit.HOURS))) {
			throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
		}

		try {
			return tryIssueCoupon(userId, couponId);
		} catch (Exception e) {
			// 실패 시 Redis에서 중복 체크 키 삭제
			redisTemplate.delete(userCouponKey);
			throw e;
		}
	}

	@DistributedLock(key = "'coupon_issue:' + #couponId")
	private IssuedCouponDto tryIssueCoupon(Long userId, Long couponId) {
		List<CouponPolicy> availablePolicies = couponPolicyRepository.findAvailablePolicies(couponId);
		if (availablePolicies.isEmpty()) {
			throw new CustomException(ErrorCode.NO_AVAILABLE_POLICY);
		}

		List<Long> policyIds = availablePolicies.stream()
			.map(CouponPolicy::getPolicyId)
			.collect(Collectors.toList());

		String couponCode = couponRedisService.issueCouponFromRedis(couponId, policyIds);

		if (couponCode == null) {
			throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
		}

		Long issuedPolicyId = Long.parseLong(couponCode.split(":")[0]);
		CouponPolicy selectedPolicy = availablePolicies.stream()
			.filter(policy -> policy.getPolicyId().equals(issuedPolicyId))
			.findFirst()
			.orElseThrow(() -> new CustomException(ErrorCode.NO_AVAILABLE_POLICY));

		selectedPolicy.issueCoupon();
		couponPolicyRepository.save(selectedPolicy);

		IssuedCoupon issuedCoupon = IssuedCoupon.of(userId, couponId, selectedPolicy.getDiscountRate());
		issuedCoupon = issuedCouponRepository.save(issuedCoupon);

		return IssuedCouponDto.from(issuedCoupon);
	}

	@Transactional
	public IssuedCouponDto useCoupon(Long userId, Long couponId) {
		IssuedCoupon issuedCoupon = issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		if (issuedCoupon.getStatus() != IssuedCouponStatus.ISSUED) {
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}

		issuedCoupon.updateStatus(IssuedCouponStatus.USED);
		return IssuedCouponDto.from(issuedCoupon);
	}

	// 사용자가 발급받은 쿠폰 목록 조회
	@Transactional(readOnly = true)
	public List<IssuedCouponDto> getIssuedCouponsByUserId(Long userId) {
		List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findActiveIssuedCouponsByUserId(userId);
		return issuedCoupons.stream()
			.map(IssuedCouponDto::from)
			.toList();
	}
}
