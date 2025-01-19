package com.linkeleven.msa.coupon.presentation.controller.external;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.service.CouponDeletionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/coupons")
public class CouponExternalController {
	private final CouponDeletionService couponDeletionService;

	// 쿠폰 삭제
	@DeleteMapping("/{feedId}")
	void deleteCoupon(
		@PathVariable Long feedId,
		@RequestHeader(value = "X-User-Id") Long userId,
		@RequestHeader(value = "X-Role") String role
	) {
		couponDeletionService.deleteCoupon(userId, role, feedId);
	}
}
