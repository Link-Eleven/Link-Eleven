package com.linkeleven.msa.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularFeedResponseDto {
	private Long userId;
	private Long feedId;
}
