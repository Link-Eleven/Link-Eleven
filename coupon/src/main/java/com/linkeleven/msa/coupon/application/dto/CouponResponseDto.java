package com.linkeleven.msa.coupon.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 관리자 및 업체 : 쿠폰 리스트 조회
public class CouponResponseDto {
	private Long couponId;
	private Long feedId;
	private Integer quantity;
	private Integer issuedCount;
	private Integer discountRate;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;
	private List<IssuedCouponDetailDto> issuedCoupons;
}
