package com.linkeleven.msa.coupon.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponPolicyDomainService {

	private final CouponPolicyRepository couponPolicyRepository;

	@Transactional
	public List<CouponPolicy> savePolicies(List<CouponPolicy> policies) {
		return couponPolicyRepository.saveAll(policies);
	}

	public List<CouponPolicy> findPoliciesByCouponIdAndStatusNot(Long couponId, CouponPolicyStatus status) {
		return couponPolicyRepository.findByCouponIdAndStatusNot(couponId, status);
	}
}