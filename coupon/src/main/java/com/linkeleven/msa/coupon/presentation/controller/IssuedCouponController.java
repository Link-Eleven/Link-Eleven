package com.linkeleven.msa.coupon.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkeleven.msa.coupon.application.dto.IssuedCouponResponseDto;
import com.linkeleven.msa.coupon.libs.dto.SuccessResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/issued-coupons")
public class IssuedCouponController {

	// 쿠폰 발급 API
	// todo: required = false 제거
	@PostMapping("/{feedId}/{couponId}/issue")
	public ResponseEntity<SuccessResponseDto<IssuedCouponResponseDto>> issueCoupon(
		@RequestHeader(value = "X-User-Id", required = false) Long userId,
		@PathVariable Long feedId,
		@PathVariable Long couponId) {

		IssuedCouponResponseDto response = IssuedCouponResponseDto.builder()
			.issuedCouponId(1L) // 임의 발급 쿠폰 ID
			.couponId(couponId) // 쿠폰 ID
			.feedId(feedId) // 게시물에 해당하는 feedId
			.userId(userId) // 발급받은 유저 ID
			.status("ISSUED") // 쿠폰 상태
			.build();

		// todo: 발급 수량 증가 로직 (발급 후 issuedCount 증가 처리)
		// todo: 서비스에서 발급 수량을 증가시켜야 합니다.

		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 발급 완료", response));
	}

	// 쿠폰 사용 API
	// todo: required = false 제거
	@PostMapping("/{issuedCouponId}/use")
	public ResponseEntity<SuccessResponseDto<IssuedCouponResponseDto>> useCoupon(
		@RequestHeader(value = "X-User-Id", required = false) Long userId, @PathVariable Long issuedCouponId) {

		IssuedCouponResponseDto response = IssuedCouponResponseDto.builder()
			.issuedCouponId(issuedCouponId) // 발급 쿠폰 ID
			.status("USED") // 쿠폰 상태 변경
			.build();
		return ResponseEntity.ok(SuccessResponseDto.success("쿠폰 사용 완료", response));
	}

	// 쿠폰 조회 API(자신의 쿠폰 목록 조회)
	@GetMapping("/my-coupons")
	// todo: required = false 제거
	public ResponseEntity<SuccessResponseDto<List<IssuedCouponResponseDto>>> getMyCoupons(
		@RequestHeader(value = "X-User-Id", required = false) Long userId) {
		// 고객이 발급받은 쿠폰 목록 조회
		List<IssuedCouponResponseDto> response = List.of(
			IssuedCouponResponseDto.builder()
				.issuedCouponId(1L) // 발급된 쿠폰 ID
				.couponId(101L)     // 쿠폰 ID
				.feedId(1L)         // 게시물 ID
				.userId(userId)     // 고객 ID
				.status("ISSUED")   // 쿠폰 상태 (발급됨)
				.build(),
			IssuedCouponResponseDto.builder()
				.issuedCouponId(2L)
				.couponId(102L)
				.feedId(2L)
				.userId(userId)
				.status("USED")     // 쿠폰 상태 (사용됨)
				.build()
		);

		return ResponseEntity.ok(SuccessResponseDto.success("본인 쿠폰 목록 조회", response));
	}
}
