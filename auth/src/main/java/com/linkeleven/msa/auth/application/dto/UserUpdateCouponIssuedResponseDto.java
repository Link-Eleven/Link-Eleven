package com.linkeleven.msa.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateCouponIssuedResponseDto {
	private Long userId;
	private Boolean isCouponIssued;

	public static UserUpdateCouponIssuedResponseDto of(Long userId, boolean isCouponIssued) {
		return UserUpdateCouponIssuedResponseDto.builder()
			.userId(userId)
			.isCouponIssued(isCouponIssued)
			.build();
	}
}
