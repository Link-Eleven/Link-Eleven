package com.linkeleven.msa.coupon.presentation.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.dto.CouponResponseDto;
import com.linkeleven.msa.coupon.application.dto.IssuedCouponDetailDto;
import com.linkeleven.msa.coupon.libs.dto.SuccessResponseDto;
import com.linkeleven.msa.coupon.presentation.request.CouponRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/coupons")
public class CouponController {

	// 쿠폰 생성
	// todo: 업체 권한, required = false 제거
	@PostMapping
	public ResponseEntity<SuccessResponseDto<CouponResponseDto>> createCoupon(
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@RequestHeader(value = "X-Role", required = false) Long role,
		@RequestBody CouponRequestDto request) {

		CouponResponseDto response = CouponResponseDto.builder()
			.couponId(1L) // 임의 쿠폰 ID
			.feedId(1L) // 임의 Feed ID
			.quantity(request.getQuantity()) // 요청에서 받은 수량
			.issuedCount(0) // 발급된 수량 (초기값) -> 쿠폰발급 api 요청 시 증가
			.discountRate(request.getDiscountRate()) // 임의 할인율
			.validFrom(LocalDateTime.now()) // 현재 시간
			.validTo(LocalDateTime.now().plusDays(30)) // 유효 기간 30일
			.build();
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 생성 완료", response));
	}

	// 업체: 쿠폰 조회
	// todo: 업체 권한, required = false 제거
	@GetMapping("/{feedId}/{couponId}")
	public ResponseEntity<SuccessResponseDto<CouponResponseDto>> getCoupon(
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@RequestHeader(value = "X-Role", required = false) Long role,
		@PathVariable Long feedId,
		@PathVariable Long couponId) {

		// 쿠폰 -> 리스트
		List<IssuedCouponDetailDto> issuedCoupons = List.of(
			IssuedCouponDetailDto.builder()
				.issuedCouponId(1L)
				.userId(123L)
				.status("ISSUED")
				.build(),
			IssuedCouponDetailDto.builder()
				.issuedCouponId(2L)
				.userId(124L)
				.status("USED")
				.build()
		);

		// 쿠폰 정보 (예시)
		CouponResponseDto response = CouponResponseDto.builder()
			.couponId(couponId)
			.feedId(feedId)
			.quantity(100)             // 임의 수량
			.issuedCount(50)           // 임의 발급된 수량
			.discountRate(40)          // 임의 할인율
			.validFrom(LocalDateTime.now())   // 유효 시작일
			.validTo(LocalDateTime.now().plusDays(30)) // 유효 종료일
			.issuedCoupons(issuedCoupons)  // 발급된 쿠폰 리스트
			.build();

		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 조회 완료", response));
	}
}