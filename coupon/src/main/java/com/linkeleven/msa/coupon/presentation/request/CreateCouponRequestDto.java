package com.linkeleven.msa.coupon.presentation.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCouponRequestDto {
	private Long feedId;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;
}
