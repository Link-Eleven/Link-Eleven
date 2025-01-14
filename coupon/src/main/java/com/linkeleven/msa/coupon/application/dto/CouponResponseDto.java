package com.linkeleven.msa.coupon.application.dto;

import java.time.LocalDateTime;

import com.linkeleven.msa.coupon.domain.model.Coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponseDto {
	private Long couponId;
	private Long feedId;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;

	public static CouponResponseDto from(Coupon coupon) {
		return CouponResponseDto.builder()
			.couponId(coupon.getCouponId())
			.feedId(coupon.getFeedId())
			.validFrom(coupon.getValidFrom())
			.validTo(coupon.getValidTo())
			.build();
	}
}
