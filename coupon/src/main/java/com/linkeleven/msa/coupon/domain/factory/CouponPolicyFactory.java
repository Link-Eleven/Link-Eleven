package com.linkeleven.msa.coupon.domain.factory;

import java.util.List;

import com.linkeleven.msa.coupon.domain.model.CouponPolicy;

public class CouponPolicyFactory {

	public static List<CouponPolicy> createPolicies(Long couponId) {
		return List.of(
			CouponPolicy.of(couponId, 40, 5),
			CouponPolicy.of(couponId, 30, 20),
			CouponPolicy.of(couponId, 20, 75)
		);
	}
}