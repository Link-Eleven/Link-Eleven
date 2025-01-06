package com.linkeleven.msa.coupon.presentation.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponRequestDto {
	private Long feedId;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;
	private List<CouponPolicyRequestDto> policies;
}
