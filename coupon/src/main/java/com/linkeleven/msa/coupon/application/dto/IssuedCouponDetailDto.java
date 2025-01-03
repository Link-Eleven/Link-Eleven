package com.linkeleven.msa.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 업체: 고객이 발급받은 쿠폰정보
public class IssuedCouponDetailDto {
	private Long issuedCouponId;  // 발급된 쿠폰 ID
	private Long userId;          // 발급받은 사용자 ID
	private String status;        // 쿠폰 상태 (ISSUED, USED)
}
