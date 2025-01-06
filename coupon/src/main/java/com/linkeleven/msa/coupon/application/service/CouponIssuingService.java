package com.linkeleven.msa.coupon.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponIssuingService {
	private final CouponPolicyRepository couponPolicyRepository;
	private final IssuedCouponRepository issuedCouponRepository;

	@Transactional
	public IssuedCouponDto issueCoupon(Long userId, String role, Long couponId) {
		// 유저가 이미 해당 쿠폰을 발급받았는지 확인
		if (issuedCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
			throw new CustomException(ErrorCode.COUPON_ALREADY_ISSUED);
		}
		// 쿠폰 정책 확인
		List<CouponPolicy> availablePolicies = couponPolicyRepository.findAvailablePolicies(couponId);

		if (availablePolicies.isEmpty()) {
			throw new CustomException(ErrorCode.NO_AVAILABLE_POLICY);
		}

		CouponPolicy selectedPolicy = availablePolicies.get(0); // 첫 번째 쿠폰정책 선택
		selectedPolicy.issueCoupon(); // 발급 가능한 수량 확인 후 증가
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
