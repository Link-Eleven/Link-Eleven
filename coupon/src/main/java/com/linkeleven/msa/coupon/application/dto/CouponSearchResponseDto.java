package com.linkeleven.msa.coupon.application.dto;

import java.time.LocalDateTime;

import com.linkeleven.msa.coupon.domain.model.enums.CouponPolicyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CouponSearchResponseDto {
	private Long couponId;
	private Long feedId;
	private CouponPolicyStatus status;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;
	private Long issuedCount;
	private Long usedCount;
}