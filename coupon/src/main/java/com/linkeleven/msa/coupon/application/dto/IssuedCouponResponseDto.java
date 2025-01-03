package com.linkeleven.msa.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

// 일반 유저용: 자신이 발급 받은 쿠폰정보
public class IssuedCouponResponseDto {
	private Long issuedCouponId;
	private Long couponId;
	private Long userId;
	private Long feedId; // 어느 피드에서 발급 받았는지 확인
	private String status; // 사용여부
}
