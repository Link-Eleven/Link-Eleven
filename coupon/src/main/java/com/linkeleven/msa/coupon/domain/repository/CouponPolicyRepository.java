package com.linkeleven.msa.coupon.domain.repository;

import java.util.List;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;

public interface CouponPolicyRepository {

	List<CouponPolicy> findAvailablePolicies(Long couponId);

	void updateCouponPolicyStatusToInactive(Long couponId, CouponPolicyStatus status);

	List<CouponPolicy> findByCouponIdAndStatusNot(Long couponId, CouponPolicyStatus status);

	List<CouponPolicy> saveAll(List<CouponPolicy> policies);

	CouponPolicy save(CouponPolicy policy);
}

