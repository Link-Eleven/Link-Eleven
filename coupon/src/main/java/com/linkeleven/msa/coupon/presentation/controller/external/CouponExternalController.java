package com.linkeleven.msa.coupon.presentation.controller.external;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.service.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/coupons")
public class CouponExternalController {
	private final CouponService couponService;

	// 쿠폰 삭제
	@DeleteMapping("/{feedId}")
	void deleteCoupon(
		@PathVariable Long feedId,
		@RequestHeader(value = "X-User-Id") Long userId,
		@RequestHeader(value = "X-Role") String role
	) {
		couponService.deleteCoupon(userId, role, feedId);
	}
}
