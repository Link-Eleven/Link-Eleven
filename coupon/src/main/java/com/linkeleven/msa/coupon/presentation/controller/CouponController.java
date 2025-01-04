package com.linkeleven.msa.coupon.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.dto.CouponResponseDto;
import com.linkeleven.msa.coupon.application.service.CouponService;
import com.linkeleven.msa.coupon.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.coupon.presentation.request.CreateCouponRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
	private final CouponService couponService;

	// 쿠폰 생성 API
	// todo: 권한 확인 추가
	@PostMapping
	public ResponseEntity<SuccessResponseDto<CouponResponseDto>> createCoupon(
		//todo: 게이트웨이 연결시 required 제거하기
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@RequestHeader(value = "X-Role", required = false) String role,
		@RequestBody CreateCouponRequestDto request) {
		CouponResponseDto coupon = couponService.createCoupon(userId, role, request);
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 생성 완료", coupon));
	}
}