package com.linkeleven.msa.coupon.application.service.validator;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.linkeleven.msa.coupon.application.service.CouponRedisService;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponValidator {
	private final CouponRedisService couponRedisService;
	private final IssuedCouponRepository issuedCouponRepository;

	public void validateIssuanceTime() {
		if (isBeforeMidnight()) {
			throw new CustomException(ErrorCode.COUPON_CANNOT_BE_ISSUED_YET);
		}
	}

	public void validateDuplicateIssuance(Long userId, Long couponId) {
		if (couponRedisService.isUserCouponAlreadyIssued(userId, couponId) ||
			issuedCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
			throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
		}
	}

	public static void validateAvailablePolicies(List<CouponPolicy> availablePolicies) {
		if (availablePolicies.isEmpty()) {
			throw new CustomException(ErrorCode.NO_AVAILABLE_POLICY);
		}
	}

	public void validateCouponStatus(IssuedCoupon issuedCoupon) {
		if (issuedCoupon.getStatus() != IssuedCouponStatus.ISSUED) {
			throw new CustomException(ErrorCode.EXPIRED_COUPON);
		}
	}

	private boolean isBeforeMidnight() {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime midnight = currentTime.toLocalDate().atStartOfDay().plusDays(1);
		return currentTime.isBefore(midnight);
	}
}
