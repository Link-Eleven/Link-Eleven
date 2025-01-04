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
	public static UserUpdateCouponIssuedResponseDto from(Long userId) {
		return UserUpdateCouponIssuedResponseDto.builder()
			.userId(userId)
			.build();
	}
}
