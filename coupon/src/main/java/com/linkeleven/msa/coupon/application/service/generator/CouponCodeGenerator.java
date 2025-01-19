package com.linkeleven.msa.coupon.application.service.generator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.linkeleven.msa.coupon.application.service.CouponRedisService;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponCodeGenerator {
	private final CouponRedisService couponRedisService;

	public String generateCode(Long couponId, List<CouponPolicy> availablePolicies) {
		List<Long> policyIds = availablePolicies.stream()
			.map(CouponPolicy::getPolicyId)
			.collect(Collectors.toList());

		return couponRedisService.issueCouponFromRedis(couponId, policyIds)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_SOLD_OUT));
	}

	public Long parsePolicyId(String couponCode) {
		try {
			return Long.parseLong(couponCode.split(":")[0]);
		} catch (NumberFormatException e) {
			throw new CustomException(ErrorCode.INVALID_COUPON_CODE);
		}
	}
}