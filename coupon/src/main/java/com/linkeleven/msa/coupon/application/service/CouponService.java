package com.linkeleven.msa.coupon.application.service;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.coupon.domain.repository.CouponRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

	private final CouponRepository couponRepository;

	public void createCoupon() {

	}
}
