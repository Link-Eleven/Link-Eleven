package com.linkeleven.msa.coupon.application.dto;

import com.linkeleven.msa.coupon.domain.model.IssuedCoupon;
import com.linkeleven.msa.coupon.domain.model.enums.IssuedCouponStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssuedCouponDto {
	private Long issuedCouponId;
	private Long userId;
	private Long couponId;
	private IssuedCouponStatus status;

	public static IssuedCouponDto from(IssuedCoupon issuedCoupon) {
		return IssuedCouponDto.builder()
			.issuedCouponId(issuedCoupon.getIssuedCouponId())
			.userId(issuedCoupon.getUserId())
			.couponId(issuedCoupon.getCouponId())
			.status(issuedCoupon.getStatus())
			.build();
	}
}
