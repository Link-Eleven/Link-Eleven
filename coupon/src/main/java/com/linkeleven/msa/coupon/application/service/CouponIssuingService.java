package com.linkeleven.msa.coupon.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.application.service.processor.CouponIssueProcessor;
import com.linkeleven.msa.coupon.application.service.validator.CouponValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponIssuingService {
	private final CouponRedisService couponRedisService;
	private final CouponValidator couponValidator;
	private final CouponIssueProcessor couponIssueProcessor;

	@Transactional
	public IssuedCouponDto issueCoupon(Long userId, String role, Long couponId) {
		couponValidator.validateIssuanceTime();
		couponValidator.validateDuplicateIssuance(userId, couponId);

		try {
			log.info("issueCoupon-try : 쿠폰 발급 시작 - userId: {}, couponId: {}", userId, couponId);
			return couponIssueProcessor.processCouponIssuance(userId, couponId);
		} catch (Exception e) {
			log.error("issueCoupon-try : 쿠폰 발급 실패 - userId: {}, couponId: {}, error: {}",
				userId, couponId, e.getMessage());
			couponRedisService.deleteUserCouponCheck(userId, couponId);
			throw e;
		}
	}
}