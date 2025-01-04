package com.linkeleven.msa.coupon.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.application.service.CouponIssuingService;
import com.linkeleven.msa.coupon.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.coupon.presentation.request.IssueCouponRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/issue-coupons")
public class IssueCouponController {

	private final CouponIssuingService couponIssuingService;

	// 쿠폰 발급 API
	@PostMapping("/{couponId}/issue")
	public ResponseEntity<SuccessResponseDto<IssuedCouponDto>> issueCoupon(
		@PathVariable Long couponId,
		@RequestBody IssueCouponRequestDto request) {
		IssuedCouponDto issuedCoupon = couponIssuingService.issueCoupon(couponId, request);
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 발급 완료", issuedCoupon));
	}
}



