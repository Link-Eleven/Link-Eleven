package com.linkeleven.msa.coupon.domain.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponDomainService {
	private final CouponRepository couponRepository;

	public Coupon createCoupon(Long feedId) {
		Coupon coupon = Coupon.of(feedId);
		return couponRepository.save(coupon);
	}

}
