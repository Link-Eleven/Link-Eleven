package com.linkeleven.msa.coupon.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponDto;
import com.linkeleven.msa.coupon.application.service.CouponIssuingService;
import com.linkeleven.msa.coupon.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/issue-coupons")
public class IssueCouponController {

	private final CouponIssuingService couponIssuingService;

	// 쿠폰 발급 API
	@PostMapping("/{couponId}/issue")
	public ResponseEntity<SuccessResponseDto<IssuedCouponDto>> issueCoupon(
		// todo: 게이트웨이 연결시 required 제거하기
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@RequestHeader(value = "X-Role", required = false) String role,
		@PathVariable Long couponId) {
		IssuedCouponDto issuedCoupon = couponIssuingService.issueCoupon(userId, couponId);
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 발급 완료", issuedCoupon));
	}

	// 쿠폰 사용 API
	@PostMapping("/{couponId}/use")
	public ResponseEntity<SuccessResponseDto<IssuedCouponDto>> useCoupon(
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@RequestHeader(value = "X-Role", required = false) String role,
		@PathVariable Long couponId) {
		IssuedCouponDto usedCoupon = couponIssuingService.useCoupon(userId, couponId);
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 사용 완료", usedCoupon));
	}

	// 유저: 발급받은 쿠폰 목록 조회 API
	@GetMapping
	public ResponseEntity<SuccessResponseDto<List<IssuedCouponDto>>> getIssuedCoupons(
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@RequestHeader(value = "X-Role", required = false) String role
	) {
		List<IssuedCouponDto> issuedCoupons = couponIssuingService.getIssuedCouponsByUserId(userId);
		return ResponseEntity.ok(SuccessResponseDto.success("발급 쿠폰 조회 완료", issuedCoupons));
	}
}



