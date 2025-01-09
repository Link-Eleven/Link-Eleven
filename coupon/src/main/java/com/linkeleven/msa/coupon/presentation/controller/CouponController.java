package com.linkeleven.msa.coupon.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.dto.CouponResponseDto;
import com.linkeleven.msa.coupon.application.dto.CouponSearchResponseDto;
import com.linkeleven.msa.coupon.application.service.CouponService;
import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;
import com.linkeleven.msa.coupon.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
	private final CouponService couponService;

	// 단건 쿠폰 조회 API
	@GetMapping("/{couponId}")
	public ResponseEntity<SuccessResponseDto<CouponResponseDto>> getCouponById(
		@PathVariable Long couponId,
		@RequestHeader(value = "X-User-Id") Long userId,
		@RequestHeader(value = "X-Role") String role
	) {
		CouponResponseDto coupon = couponService.getCouponById(userId, role, couponId);
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 조회 완료", coupon));
	}

	// Master & Company: 쿠폰 검색 API
	@GetMapping("/search")
	public ResponseEntity<SuccessResponseDto<Page<CouponSearchResponseDto>>> searchCoupons(
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@RequestHeader(value = "X-Role", required = false) String role,
		@RequestParam(required = false) CouponPolicyStatus status,
		@RequestParam(required = false) Long feedId,
		@RequestParam(required = false) String validFrom,
		@RequestParam(required = false) String validTo,
		Pageable pageable) {
		Page<CouponSearchResponseDto> couponList = couponService.searchCoupons(userId, role, status, feedId, validFrom,
			validTo,
			pageable);
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 조회 완료", couponList));
	}
}