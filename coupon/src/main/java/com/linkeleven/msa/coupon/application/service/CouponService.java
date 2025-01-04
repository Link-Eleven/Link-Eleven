package com.linkeleven.msa.coupon.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linkeleven.msa.coupon.application.dto.CouponResponseDto;
import com.linkeleven.msa.coupon.domain.model.Coupon;
import com.linkeleven.msa.coupon.domain.model.CouponPolicy;
import com.linkeleven.msa.coupon.domain.repository.CouponPolicyRepository;
import com.linkeleven.msa.coupon.domain.repository.CouponRepository;
import com.linkeleven.msa.coupon.libs.exception.CustomException;
import com.linkeleven.msa.coupon.libs.exception.ErrorCode;
import com.linkeleven.msa.coupon.presentation.request.CreateCouponRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final CouponPolicyRepository couponPolicyRepository;

	// 쿠폰 생성
	public CouponResponseDto createCoupon(Long userId, String role, CreateCouponRequestDto request) {
		// todo: 컨트롤러 require 제거시 null 체크 제거하기
		if (role == null || role.equals("USER")) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		Coupon coupon = Coupon.of(request.getFeedId(), request.getValidFrom(), request.getValidTo());
		coupon = couponRepository.save(coupon);

		// 쿠폰 정책 생성
		List<CouponPolicy> policies = List.of(
			CouponPolicy.of(coupon.getCouponId(), 40, 5), // 40% 쿠폰 5장
			CouponPolicy.of(coupon.getCouponId(), 30, 20), // 30% 쿠폰 20장
			CouponPolicy.of(coupon.getCouponId(), 20, 75) // 20% 쿠폰 75장
		);
		couponPolicyRepository.saveAll(policies);
		return CouponResponseDto.from(coupon);
	}

}
