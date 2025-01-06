package com.linkeleven.msa.coupon.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponPolicyRequestDto {
	private int discountRate;
	private int quantity;
}
