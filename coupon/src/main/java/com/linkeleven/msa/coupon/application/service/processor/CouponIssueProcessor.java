package com.linkeleven.msa.coupon.application.service.processor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.application.service.CouponRedisService;
import com.linkeleven.msa.coupon.application.service.generator.CouponCodeGenerator;
import com.linkeleven.msa.coupon.application.service.validator.CouponValidator;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.IssuedCouponRepository;
import com.linkeleven.msa.coupon.infrastructure.aop.DistributedLock;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponIssueProcessor {
	private final CouponPolicyRepository couponPolicyRepository;
	private final IssuedCouponRepository issuedCouponRepository;
	private final CouponRedisService couponRedisService;
	private final CouponCodeGenerator couponCodeGenerator;

	@DistributedLock(key = "'coupon_issue:' + #couponId")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public IssuedCouponDto processCouponIssuance(Long userId, Long couponId) {
		List<CouponPolicy> availablePolicies = couponPolicyRepository.findAvailablePolicies(couponId);
		CouponValidator.validateAvailablePolicies(availablePolicies);

		String couponCode = couponCodeGenerator.generateCode(couponId, availablePolicies);
		Long policyId = couponCodeGenerator.parsePolicyId(couponCode);

		CouponPolicy selectedPolicy = findAndUpdatePolicy(availablePolicies, policyId);
		return createAndSaveIssuedCoupon(userId, couponId, selectedPolicy);
	}

	private CouponPolicy findAndUpdatePolicy(List<CouponPolicy> policies, Long policyId) {
		CouponPolicy policy = policies.stream()
			.filter(p -> p.getPolicyId().equals(policyId))
			.findFirst()
			.orElseThrow(() -> new CustomException(ErrorCode.NO_AVAILABLE_POLICY));

		policy.issueCoupon();
		return couponPolicyRepository.save(policy);
	}

	private IssuedCouponDto createAndSaveIssuedCoupon(Long userId, Long couponId, CouponPolicy policy) {
		IssuedCoupon issuedCoupon = IssuedCoupon.of(userId, couponId, policy.getDiscountRate());
		return IssuedCouponDto.from(issuedCouponRepository.save(issuedCoupon));
	}
}