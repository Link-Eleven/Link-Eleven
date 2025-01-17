package com.linkeleven.msa.coupon.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.application.service.validator.CouponValidator;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponUsageService {
	private final IssuedCouponRepository issuedCouponRepository;
	private final CouponValidator couponValidator;

	@Transactional
	public IssuedCouponDto useCoupon(Long userId, Long couponId) {
		IssuedCoupon issuedCoupon = issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		couponValidator.validateCouponStatus(issuedCoupon);
		issuedCoupon.markAsUsed();

		return IssuedCouponDto.from(issuedCoupon);
	}
}