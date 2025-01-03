package com.linkeleven.msa.coupon.presentation.request;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CouponRequestDto {
	private Long feedId; // 게시글 ID
	private int quantity; // 쿠폰 수량
	private int discountRate; // 할인율
	private LocalDateTime validFrom; // 유효 시작일
	private LocalDateTime validTo; // 유효 만료일
}
