package com.linkeleven.msa.coupon.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.domain.service.CouponPolicyDomainService;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponDeletionService {
	private final CouponRepository couponRepository;
	private final CouponPolicyDomainService couponPolicyDomainService;
	private final IssuedCouponRepository issuedCouponRepository;

	@Transactional
	public void deleteCoupon(Long userId, String role, Long feedId) {
		validateRoleForDeletion(role);
		Coupon coupon = couponRepository.findByFeedId(feedId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
		softDeleteCouponWithPoliciesAndIssuedCoupons(userId, coupon);
	}

	private void validateRoleForDeletion(String role) {
		if ("USER".equals(role)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
	}

	private void softDeleteCouponWithPoliciesAndIssuedCoupons(Long userId, Coupon coupon) {
		softDeletePolicies(userId, coupon);
		softDeleteIssuedCoupons(userId, coupon);
		coupon.softDelete(userId);
	}

	private void softDeletePolicies(Long userId, Coupon coupon) {
		List<CouponPolicy> policies = couponPolicyDomainService.findPoliciesByCouponIdAndStatusNot(
			coupon.getCouponId(), CouponPolicyStatus.DELETED);
		policies.forEach(policy -> policy.softDelete(userId));
	}

	private void softDeleteIssuedCoupons(Long userId, Coupon coupon) {
		List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findByCouponIdAndStatusNot(
			coupon.getCouponId(), IssuedCouponStatus.DELETED);
		issuedCoupons.forEach(issuedCoupon -> issuedCoupon.softDelete(userId));
	}
}
